package kr.co.api.notification.dto;

import java.util.UUID;

public record NotificationItemDto(
        UUID notificationId,
        UUID senderId,
        UUID referenceId
) {

}
