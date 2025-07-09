package kr.co.api.event.listener;

import kr.co.api.notification.NotificationSendService;
import kr.co.api.notification.dto.NotificationSendDto;
import kr.co.domain.common.event.NotificationSendEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationSendService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFileRollback(NotificationSendEvent event) {
        NotificationSendDto dto = new NotificationSendDto();

        try {
            notificationService.send(dto);
        } catch (Exception e) {
            log.error("알림 보내기 실패 {}:", e.getMessage());
        }
    }
}
