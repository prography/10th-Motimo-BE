package kr.co.api.group.service.message;

import java.util.Map;
import java.util.UUID;
import kr.co.domain.group.message.strategy.MessageContentDataProvider;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.user.model.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageContentData implements MessageContentDataProvider {

    private final Map<UUID, Todo> todoMap;
    private final Map<UUID, TodoResult> todoResultMap;
    private final Map<UUID, User> userMap;

    public Todo getTodo(UUID todoId) {
        return todoMap.get(todoId);
    }

    public TodoResult getTodoResult(UUID todoResultId) {
        return todoResultMap.get(todoResultId);
    }

    public User getUser(UUID userId) {
        return userMap.get(userId);
    }
}
