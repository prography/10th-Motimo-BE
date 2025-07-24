package kr.co.api.notification.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import kr.co.api.notification.strategy.NotificationDataCollector;
import kr.co.api.notification.strategy.NotificationDataContext;
import kr.co.api.notification.strategy.NotificationStrategy;
import kr.co.api.notification.strategy.NotificationStrategyFactory;
import kr.co.domain.notification.Notification;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class NotificationAssembler {
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final NotificationStrategyFactory strategyFactory;

    public Map<UUID, String> convertNotificationContent(List<Notification> notifications) {
        // 1. 모든 전략을 이용해 필요한 데이터 ID 수집
        NotificationDataCollector collector = collectRequiredData(notifications);

        // 2. 수집된 ID로 한 번에 데이터 조회
        NotificationDataContext context = loadDataContext(collector);

        // 3. 각 알림별로 적절한 전략을 사용해 제목 생성
        return generateTitles(notifications, context);
    }

    private NotificationDataCollector collectRequiredData(List<Notification> notifications) {
        NotificationDataCollector collector = new NotificationDataCollector();

        for (Notification notification : notifications) {
            NotificationStrategy strategy = strategyFactory.getStrategy(notification.getType());
            strategy.collectRequiredIds(notification, collector);
        }

        return collector;
    }

    private NotificationDataContext loadDataContext(NotificationDataCollector collector) {
        // 필요한 데이터만 조회
        Map<UUID, User> senderMap = collector.getSenderIds().isEmpty()
                ? Collections.emptyMap()
                : userRepository.findAllByIdsIn(collector.getSenderIds()).stream()
                        .collect(Collectors.toMap(User::getId, Function.identity()));

        Map<UUID, Todo> todoMap = collector.getTodoIds().isEmpty()
                ? Collections.emptyMap()
                : todoRepository.findAllByIdsIn(collector.getTodoIds()).stream()
                        .collect(Collectors.toMap(Todo::getId, Function.identity()));

        return NotificationDataContext.builder()
                .senderMap(senderMap)
                .todoMap(todoMap)
                .build();
    }

    private Map<UUID, String> generateTitles(List<Notification> notifications,
            NotificationDataContext context) {
        Map<UUID, String> titleMap = new HashMap<>();

        for (Notification notification : notifications) {
            try {
                NotificationStrategy strategy = strategyFactory.getStrategy(notification.getType());
                String title = strategy.generateTitle(notification, context);
                titleMap.put(notification.getId(), title);
            } catch (Exception e) {
                // 로깅 후 기본 제목 사용
                log.warn("알림 제목 생성 실패. notificationId: {}, type: {}",
                        notification.getId(), notification.getType(), e);
                titleMap.put(notification.getId(), notification.getType().getDefaultTitle());
            }
        }

        return titleMap;
    }
}
