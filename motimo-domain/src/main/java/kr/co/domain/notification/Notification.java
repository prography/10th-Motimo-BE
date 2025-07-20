package kr.co.domain.notification;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification {
    @Builder.Default()
    private UUID id = null;
    private UUID senderId;
    private UUID receiverId;
    private UUID referenceId;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;

    @Builder(builderMethodName = "createNotification")
    private Notification(UUID senderId, UUID receiverId, UUID referenceId, NotificationType type) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.referenceId = referenceId;
        this.type = type;
    }
}
