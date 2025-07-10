package kr.co.domain.notification.repository;

import java.util.UUID;
import kr.co.domain.notification.Notification;

public interface NotificationRepository {
    UUID save(Notification notification);
}
