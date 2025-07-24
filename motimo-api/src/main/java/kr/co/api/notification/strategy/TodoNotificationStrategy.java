package kr.co.api.notification.strategy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import kr.co.domain.notification.Notification;
import kr.co.domain.notification.NotificationTitleResolver;
import kr.co.domain.notification.NotificationType;
import kr.co.domain.todo.Todo;
import org.springframework.stereotype.Component;

@Component
class TodoNotificationStrategy implements NotificationStrategy {

    @Override
    public Set<NotificationType> getSupportedTypes() {
        return Set.of(NotificationType.TODO_DUE_DAY);
    }

    @Override
    public void collectRequiredIds(Notification notification, NotificationDataCollector collector) {
        collector.addTodoId(notification.getReferenceId());
    }

    @Override
    public String generateTitle(Notification notification, NotificationDataContext context) {
        Todo todo = context.getTodo(notification.getReferenceId());
        String todoTitle = todo != null ? todo.getTitle() : "알 수 없는 투두";
        String day = calculateRemainingDays(todo, notification.getCreatedAt());

        Map<String, String> variables = Map.of(
                "todoTitle", todoTitle,
                "day", day
        );
        return NotificationTitleResolver.resolveTitle(notification.getType(), variables);
    }

    private String calculateRemainingDays(Todo todo, LocalDateTime notificationTime) {
        if (todo == null || todo.getDate() == null) {
            return "1";
        }

        LocalDate notificationDate = notificationTime.toLocalDate();

        long daysBetween = ChronoUnit.DAYS.between(notificationDate, todo.getDate());
        return String.valueOf(Math.max(1, daysBetween));
    }
}
