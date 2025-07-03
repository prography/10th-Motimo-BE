package kr.co.api.group.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.group.Group;
import kr.co.domain.group.exception.UnsupportedGroupMessageTypeException;
import kr.co.domain.group.message.GroupJoinContent;
import kr.co.domain.group.message.GroupLeaveContent;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.GroupMessageContent;
import kr.co.domain.group.message.GroupMessageType;
import kr.co.domain.group.message.MessageReference;
import kr.co.domain.group.message.TodoCompletedContent;
import kr.co.domain.group.message.TodoResultSubmittedContent;
import kr.co.domain.group.message.repository.GroupMessageRepository;
import kr.co.domain.group.repository.GroupRepository;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.todo.exception.TodoNotFoundException;
import kr.co.domain.todo.exception.TodoResultNotSubmittedException;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.domain.todo.repository.TodoResultRepository;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupMessageQueryService {

    private final GroupRepository groupRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final TodoResultRepository todoResultRepository;

    public CustomSlice<GroupMessageDto> findAllByGroupId(UUID userId, UUID groupId, int offset,
            int limit) {
        Group group = groupRepository.findById(groupId);
        // TODO: group에 참여하고 있는 유저인지 확인 로직 필요

        User user = userRepository.findById(userId);

        CustomSlice<GroupMessage> groupMessages = groupMessageRepository.findAllByGroupId(groupId,
                offset, limit);

        // 성능 최적화: 필요한 데이터들을 미리 배치로 조회
        Map<UUID, Todo> todoMap = getTodos(groupMessages.content());
        Map<UUID, TodoResult> todoResultMap = getTodoResults(groupMessages.content());

        // GroupMessage를 GroupMessageDto로 변환
        return groupMessages.map(groupMessage ->
                convertToGroupMessageDto(groupMessage, userId, todoMap, todoResultMap));
    }

    private GroupMessageDto convertToGroupMessageDto(GroupMessage groupMessage, UUID userId,
            Map<UUID, Todo> todoMap, Map<UUID, TodoResult> todoResultMap) {

        // 메시지 타입에 따라 적절한 GroupMessageContent 생성
        GroupMessageContent content = createMessageContent(groupMessage, todoMap, todoResultMap);

        // 현재 사용자의 리액션 여부 확인
        boolean hasUserReacted = groupMessage.getReactions().stream()
                .anyMatch(reaction -> reaction.getUserId().equals(userId));

        User user = userRepository.findById(groupMessage.getUserId());

        return new GroupMessageDto(
                groupMessage.getId(),
                groupMessage.getUserId(),
                user.getNickname(),
                groupMessage.getMessageReference(),
                content,
                groupMessage.getReactionCount(),
                hasUserReacted,
                groupMessage.getSendAt()
        );
    }

    private GroupMessageContent createMessageContent(GroupMessage groupMessage,
            Map<UUID, Todo> todoMap, Map<UUID, TodoResult> todoResultMap) {

        GroupMessageType messageType = groupMessage.getMessageType();
        MessageReference messageReference = groupMessage.getMessageReference();

        return switch (messageType) {
            case JOIN -> new GroupJoinContent();

            case LEAVE -> new GroupLeaveContent();

            case TODO_COMPLETE -> {
                UUID todoId = messageReference.referenceId();
                Todo todo = todoMap.get(todoId);
                if (todo == null) {
                    throw new TodoNotFoundException();
                }
                yield new TodoCompletedContent(todoId, todo.getTitle());
            }

            case TODO_RESULT_SUBMIT -> {
                UUID todoResultId = messageReference.referenceId();
                TodoResult todoResult = todoResultMap.get(todoResultId);
                if (todoResult == null) {
                    throw new TodoResultNotSubmittedException();
                }

                Todo todo = todoMap.get(todoResult.getTodoId());
                if (todo == null) {
                    throw new TodoNotFoundException();
                }

                yield new TodoResultSubmittedContent(
                        todoResult.getTodoId(),
                        todo.getTitle(),
                        todoResultId,
                        todoResult.getEmotion(),
                        todoResult.getContent(),
                        todoResult.getFilePath()
                );
            }

            default -> throw new UnsupportedGroupMessageTypeException();
        };
    }

    // 필요한 투두들을 한 번에 조회
    private Map<UUID, Todo> getTodos(List<GroupMessage> messages) {
        // TODO_COMPLETE 메시지에서는 투두 ID들 필요
        Set<UUID> todoIds = messages.stream()
                .filter(msg -> msg.getMessageType() == GroupMessageType.TODO_COMPLETE)
                .map(msg -> msg.getMessageReference().referenceId())
                .collect(Collectors.toSet());

        // TODO_RESULT_SUBMIT 메시지에서 필요한 투두 ID들도 수집
        Set<UUID> todoResultIds = messages.stream()
                .filter(msg -> msg.getMessageType() == GroupMessageType.TODO_RESULT_SUBMIT)
                .map(msg -> msg.getMessageReference().referenceId())
                .collect(Collectors.toSet());

        if (!todoResultIds.isEmpty()) {
            List<TodoResult> todoResults = todoResultRepository.findAllByIdsIn(todoResultIds);
            Set<UUID> additionalTodoIds = todoResults.stream()
                    .map(TodoResult::getTodoId)
                    .collect(Collectors.toSet());
            todoIds.addAll(additionalTodoIds);
        }

        if (todoIds.isEmpty()) {
            return Map.of();
        }

        return todoRepository.findAllByIdsIn(todoIds).stream()
                .collect(Collectors.toMap(Todo::getId, todo -> todo));
    }

    // 필요한 투두 결과들을 한 번에 조회
    private Map<UUID, TodoResult> getTodoResults(List<GroupMessage> messages) {
        Set<UUID> todoResultIds = messages.stream()
                .filter(msg -> msg.getMessageType() == GroupMessageType.TODO_RESULT_SUBMIT)
                .map(msg -> msg.getMessageReference().referenceId())
                .collect(Collectors.toSet());

        if (todoResultIds.isEmpty()) {
            return Map.of();
        }

        return todoResultRepository.findAllByIdsIn(todoResultIds).stream()
                .collect(Collectors.toMap(TodoResult::getId, todoResult -> todoResult));
    }

}
