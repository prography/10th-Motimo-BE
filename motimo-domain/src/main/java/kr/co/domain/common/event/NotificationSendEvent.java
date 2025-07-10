package kr.co.domain.common.event;

import java.util.UUID;
import kr.co.domain.notification.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationSendEvent extends Event{
    private NotificationType type;
    private UUID senderId;
    private UUID receiverId;
    private UUID referenceId;
    private String title;
    private String content;

    public NotificationSendEvent(NotificationType type, UUID senderId, UUID receiverId, String title, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.title = title;
        this.content = content;
    }
}
