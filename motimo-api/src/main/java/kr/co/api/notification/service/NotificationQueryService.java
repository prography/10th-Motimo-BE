package kr.co.api.notification.service;

import java.util.Map;
import java.util.UUID;
import kr.co.api.notification.dto.NotificationItemDto;
import kr.co.domain.common.pagination.CustomPage;
import kr.co.domain.notification.Notification;
import kr.co.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationQueryService {
    private final NotificationRepository notificationRepository;
    private final NotificationAssembler assembler;

    public CustomPage<NotificationItemDto> getNotificationList(UUID userId, int page, int size) {
        CustomPage<Notification> pageNotifications = notificationRepository.findAllByReceiverId(userId, page, size);

        Map<UUID, String> notificationContentMap = assembler.convertNotificationContent(pageNotifications.content());

        return pageNotifications.map(notification -> NotificationItemDto.of(notification, notificationContentMap.get(notification.getId())));
    }
}
