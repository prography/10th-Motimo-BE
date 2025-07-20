package kr.co.domain.common.event;

import java.util.UUID;
import kr.co.domain.notification.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationSentEvent extends Event{
    private NotificationType type;
    private UUID senderId;
    private UUID receiverId;
    private UUID referenceId;
    private String title;

    public NotificationSentEvent(NotificationType type, UUID senderId, UUID receiverId, UUID referenceId, String title) {
        super();
        this.type = type;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.referenceId = referenceId;
        this.title = title;
    }
}
