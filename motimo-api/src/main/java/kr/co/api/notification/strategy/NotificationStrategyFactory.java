package kr.co.api.notification.strategy;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kr.co.domain.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationStrategyFactory {
    private final List<NotificationStrategy> strategies;
    private final Map<NotificationType, NotificationStrategy> strategyMap = new HashMap<>();

    @PostConstruct
    private void initializeStrategyMap() {
        for (NotificationStrategy strategy : strategies) {
            for (NotificationType type : strategy.getSupportedTypes()) {
                strategyMap.put(type, strategy);
            }
        }
    }

    public NotificationStrategy getStrategy(NotificationType type) {
        NotificationStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 알림 타입입니다: " + type);
        }
        return strategy;
    }

    public Set<NotificationStrategy> getAllStrategies() {
        return new HashSet<>(strategies);
    }
}