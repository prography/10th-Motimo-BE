package kr.co.api.notification.strategy;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;

@Getter
public class NotificationDataCollector {
    private final Set<UUID> senderIds = new HashSet<>();
    private final Set<UUID> todoIds = new HashSet<>();

    public void addSenderId(UUID senderId) {
        if (senderId != null) {
            senderIds.add(senderId);
        }
    }

    public void addTodoId(UUID todoId) {
        if (todoId != null) {
            todoIds.add(todoId);
        }
    }
}