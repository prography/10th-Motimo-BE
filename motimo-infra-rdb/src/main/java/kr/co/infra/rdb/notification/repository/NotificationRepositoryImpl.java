package kr.co.infra.rdb.notification.repository;

import java.util.UUID;
import kr.co.domain.notification.Notification;
import kr.co.domain.notification.repository.NotificationRepository;
import kr.co.infra.rdb.notification.NotificationEntity;
import kr.co.infra.rdb.notification.util.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;

    public UUID save(Notification notification) {
        NotificationEntity entity = notificationJpaRepository.save(
                NotificationMapper.toEntity(notification)
        );

        return entity.getId();
    }
}
