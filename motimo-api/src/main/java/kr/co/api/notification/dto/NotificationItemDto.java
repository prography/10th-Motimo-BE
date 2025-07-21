package kr.co.api.notification.dto;

import java.util.UUID;
import kr.co.domain.notification.Notification;
import kr.co.domain.notification.NotificationType;

public record NotificationItemDto(
        UUID notificationId,
        String content,
        UUID referenceId,
        NotificationType type,
        boolean isRead
) {

    public static NotificationItemDto of(Notification notification, String content) {
        return new NotificationItemDto(
                notification.getId(),
                content,
                notification.getReferenceId(),
                notification.getType(),
                notification.isRead()
        );
    }
}
