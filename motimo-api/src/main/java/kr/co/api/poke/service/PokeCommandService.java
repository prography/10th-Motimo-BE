package kr.co.api.poke.service;

import java.util.UUID;
import kr.co.domain.common.event.Events;
import kr.co.domain.common.event.NotificationSentEvent;
import kr.co.domain.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PokeCommandService {
    public void createPokeNotification(UUID userId, UUID receiverId) {
        Events.publishEvent(new NotificationSentEvent(
                NotificationType.POKE,
                userId,
                receiverId,
                null,
                NotificationType.POKE.getDefaultTitle(),
                NotificationType.POKE.getDefaultContent()
        ));
    }
}
