package kr.co.api.group.service.message;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import kr.co.domain.group.exception.MessageContentLoadingException;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.MessageReference;
import kr.co.domain.group.message.MessageReferenceType;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.domain.todo.repository.TodoResultRepository;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageContentLoader {

    private final TodoRepository todoRepository;
    private final TodoResultRepository todoResultRepository;
    private final UserRepository userRepository;

    public MessageContentData loadMessageContentData(List<GroupMessage> messages) {

        try {
            EnumMap<MessageReferenceType, Set<UUID>> referenceTypeIds =
                    new EnumMap<>(MessageReferenceType.class);

            referenceTypeIds.put(MessageReferenceType.TODO, new HashSet<>());
            referenceTypeIds.put(MessageReferenceType.TODO_RESULT, new HashSet<>());

            // 메시지 타입별로 필요한 ID들 추출
            Set<UUID> userIds = messages.stream()
                    .map(GroupMessage::getUserId)
                    .collect(Collectors.toSet());

            for (GroupMessage m : messages) {
                MessageReference msgReference = m.getMessageReference();
                if (msgReference != null) {
                    referenceTypeIds.get(msgReference.messageReferenceType())
                            .add(msgReference.referenceId());
                }
            }

            // 배치 조회 실행
            Map<UUID, TodoResult> todoResultMap = loadTodoResults(
                    referenceTypeIds.get(MessageReferenceType.TODO_RESULT));

            Map<UUID, Todo> todoMap = loadTodos(
                    referenceTypeIds.get(MessageReferenceType.TODO), todoResultMap);

            Map<UUID, User> userMap = loadUsers(userIds);

            return new MessageContentData(todoMap, todoResultMap, userMap);
        } catch (DataAccessException e) {
            log.error("메시지 content 로드중 Database 에러가 발생했습니다 - {} messages", messages.size(), e);
            throw new MessageContentLoadingException();
        } catch (Exception e) {
            log.error("메시지 content 로드 중 예상하지 못한 에러가 발생했습니다 - {} messages", messages.size(), e);
            throw new MessageContentLoadingException();
        }
    }

    private Map<UUID, Todo> loadTodos(Set<UUID> todoIds, Map<UUID, TodoResult> todoResults) {

        Set<UUID> todoIdSet = new HashSet<>(todoIds);

        // TodoResult에 필요한 투두 ID들도 추가
        todoIdSet.addAll(todoResults.values().stream().map(TodoResult::getTodoId).toList());

        if (todoIdSet.isEmpty()) {
            return Map.of();
        }

        return todoRepository.findAllByIdsIn(todoIdSet).stream()
                .collect(Collectors.toMap(Todo::getId, todo -> todo));
    }

    private Map<UUID, TodoResult> loadTodoResults(Set<UUID> todoResultIds) {
        if (todoResultIds.isEmpty()) {
            return Map.of();
        }

        return todoResultRepository.findAllByIdsIn(todoResultIds).stream()
                .collect(Collectors.toMap(TodoResult::getId, todoResult -> todoResult));
    }

    private Map<UUID, User> loadUsers(Set<UUID> userIds) {
        if (userIds.isEmpty()) {
            return Map.of();
        }

        return userRepository.findAllByIdsIn(userIds).stream()
                .collect(Collectors.toMap(User::getId, user -> user));
    }
}
