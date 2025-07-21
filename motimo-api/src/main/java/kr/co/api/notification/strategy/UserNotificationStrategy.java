package kr.co.api.notification.strategy;

import java.util.Map;
import java.util.Set;
import kr.co.domain.notification.Notification;
import kr.co.domain.notification.NotificationTitleResolver;
import kr.co.domain.notification.NotificationType;
import kr.co.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserNotificationStrategy implements NotificationStrategy {

    @Override
    public Set<NotificationType> getSupportedTypes() {
        return Set.of(
                NotificationType.POKE,
                NotificationType.REACTION,
                NotificationType.GROUP_TODO_COMPLETED,
                NotificationType.GROUP_TODO_RESULT_COMPLETED
        );
    }

    @Override
    public void collectRequiredIds(Notification notification, NotificationDataCollector collector) {
        if(notification.getSenderId() != null) {
            collector.addSenderId(notification.getSenderId());
        }
    }

    @Override
    public String generateTitle(Notification notification, NotificationDataContext context) {
        User sender = context.getSender(notification.getSenderId());
        String senderName = sender != null ? sender.getNickname() : "알 수 없는 사용자";

        Map<String, String> variables = Map.of("name", senderName);
        return NotificationTitleResolver.resolveTitle(notification.getType(), variables);
    }
}