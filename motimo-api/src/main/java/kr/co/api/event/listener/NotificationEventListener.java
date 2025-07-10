package kr.co.api.event.listener;

import kr.co.api.notification.dto.NotificationSendDto;
import kr.co.api.notification.service.NotificationCommandService;
import kr.co.api.notification.service.NotificationSendService;
import kr.co.domain.common.event.NotificationSendEvent;
import kr.co.domain.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationSendService notificationService;
    private final NotificationCommandService notificationCommandService;

    @EventListener
    public void handleNotificationSend(NotificationSendEvent event) {
        Notification notification = Notification.createNotification()
                .senderId(event.getSenderId())
                .receiverId(event.getReceiverId())
                .referenceId(event.getReferenceId())
                .title(event.getTitle())
                .content(event.getContent())
                .type(event.getType()).build();


        NotificationSendDto dto = new NotificationSendDto();

        try {
            notificationCommandService.save(notification);
            notificationService.send(dto);
        } catch (Exception e) {
            log.error("알림 보내기 실패 {}:", e.getMessage());
        }
    }
}
