package kr.co.api.notification.strategy;

import java.util.Map;
import java.util.UUID;
import kr.co.domain.todo.Todo;
import kr.co.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationDataContext {
    private final Map<UUID, User> senderMap;
    private final Map<UUID, Todo> todoMap;

    public User getSender(UUID senderId) {
        return senderMap.get(senderId);
    }

    public Todo getTodo(UUID todoId) {
        return todoMap.get(todoId);
    }
}