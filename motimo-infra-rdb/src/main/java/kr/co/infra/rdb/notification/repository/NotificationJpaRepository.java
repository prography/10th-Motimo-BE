package kr.co.infra.rdb.notification.repository;

import java.util.UUID;
import kr.co.infra.rdb.notification.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, UUID> {
}
