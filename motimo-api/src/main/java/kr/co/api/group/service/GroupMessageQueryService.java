package kr.co.api.group.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.group.Group;
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
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.domain.todo.repository.TodoResultRepository;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
        // User user = userRepository.findById(userId);

        CustomSlice<GroupMessage> groupMessages = groupMessageRepository.findAllByGroupId(groupId,
                offset, limit);

        // 메시지에 필요한 데이터들을 미리 한꺼번에 조회
        Map<UUID, TodoResult> todoResultMap = getTodoResults(groupMessages.content());
        Map<UUID, Todo> todoMap = getTodos(groupMessages.content(), todoResultMap);
        Map<UUID, User> userMap = getUsers(groupMessages.content());

        // GroupMessage를 GroupMessageDto로 변환
        return groupMessages.map(groupMessage -> {
            // 메시지 타입에 따라 적절한 GroupMessageContent 생성
            GroupMessageContent content = createMessageContent(groupMessage, todoMap,
                    todoResultMap);

            // 현재 사용자의 리액션 여부 확인
            boolean hasUserReacted = groupMessage.getReactions().stream()
                    .anyMatch(reaction -> reaction.getUserId().equals(userId));

            // 메시지를 작성한 유저 이름
            String userName = userMap.get(groupMessage.getUserId()).getNickname();
            return GroupMessageDto.of(groupMessage, userName, content, hasUserReacted);
        });
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
                    log.warn("Todo(id={}) not found for TODO_COMPLETE message {}", todoId,
                            groupMessage.getId());
                    yield null;
                }
                yield new TodoCompletedContent(todoId, todo.getTitle());
            }

            case TODO_RESULT_SUBMIT -> {
                UUID todoResultId = messageReference.referenceId();
                TodoResult todoResult = todoResultMap.get(todoResultId);
                if (todoResult == null) {
                    log.warn("TodoResult(id={}) not found for TODO_RESULT_SUBMIT message {}",
                            todoResultId,
                            groupMessage.getId());
                    yield null;
                }

                UUID todoId = todoResult.getTodoId();
                Todo todo = todoMap.get(todoId);
                if (todo == null) {
                    log.warn("Todo(id={}) not found for TODO_RESULT_SUBMIT message {}", todoId,
                            groupMessage.getId());
                    yield null;
                }

                yield new TodoResultSubmittedContent(
                        todoId,
                        todo.getTitle(),
                        todoResultId,
                        todoResult.getEmotion(),
                        todoResult.getContent(),
                        todoResult.getFilePath()
                );
            }

            default -> null;
        };
    }

    // 필요한 투두들을 한 번에 조회
    private Map<UUID, Todo> getTodos(List<GroupMessage> messages,
            Map<UUID, TodoResult> todoResultMap) {
        // TODO_COMPLETE 메시지에서는 투두 ID들 필요
        Set<UUID> todoIds = messages.stream()
                .filter(msg -> msg.getMessageType() == GroupMessageType.TODO_COMPLETE)
                .map(msg -> msg.getMessageReference().referenceId())
                .collect(Collectors.toSet());

        // TODO_RESULT_SUBMIT 메시지에서 필요한 투두 ID들도 수집
        todoIds.addAll(todoResultMap.values().stream().map(TodoResult::getTodoId).toList());

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

    private Map<UUID, User> getUsers(List<GroupMessage> messages) {
        Set<UUID> userIds = messages.stream().map(GroupMessage::getUserId)
                .collect(Collectors.toSet());

        if (userIds.isEmpty()) {
            return Map.of();
        }

        return userRepository.findAllByIdsIn(userIds).stream()
                .collect(Collectors.toMap(User::getId, user -> user));
    }
}
