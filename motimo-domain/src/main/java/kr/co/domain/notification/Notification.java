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
    private UUID targetId;
    private NotificationType type;
}
