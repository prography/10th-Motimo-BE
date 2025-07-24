package kr.co.api.notification.strategy;

import java.util.Set;
import kr.co.domain.notification.Notification;
import kr.co.domain.notification.NotificationType;

public interface NotificationStrategy {
    /**
     * 해당 전략이 처리할 수 있는 알림 타입들을 반환
     */
    Set<NotificationType> getSupportedTypes();

    /**
     * 필요한 데이터의 ID를 수집
     */
    void collectRequiredIds(Notification notification, NotificationDataCollector collector);

    String generateTitle(Notification notification, NotificationDataContext context);
}
