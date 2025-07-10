package kr.co.api.notification.service;

import java.util.List;
import java.util.UUID;
import kr.co.api.notification.dto.NotificationItemDto;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationQueryService {
    private final NotificationRepository notificationRepository;

    public CustomSlice<NotificationItemDto> readNotificationList(UUID userId, int offset, int limit) {
        List<NotificationItemDto> notificationListRs = List.of(
                new NotificationItemDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()),
                new NotificationItemDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        );

        return new CustomSlice<>(notificationListRs, true, offset, limit);
    }
}
