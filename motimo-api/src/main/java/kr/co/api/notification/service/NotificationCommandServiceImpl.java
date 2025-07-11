package kr.co.api.notification.service;

import kr.co.domain.notification.Notification;
import kr.co.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationCommandServiceImpl implements NotificationCommandService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public void save(Notification notification) {
        notificationRepository.create(notification);
    }
}
