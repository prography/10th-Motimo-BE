package kr.co.domain.notification;

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
    private String title;
    private String content;
    private NotificationType type;

    @Builder(builderMethodName = "createNotification")
    private Notification(UUID senderId, UUID receiverId, UUID referenceId, String title, String content, NotificationType type) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.referenceId = referenceId;
        this.title = title;
        this.content = content;
        this.type = type;
    }
}
