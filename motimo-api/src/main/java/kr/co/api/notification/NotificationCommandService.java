package kr.co.api.notification;

import kr.co.domain.notification.Notification;

public interface NotificationCommandService {
    void save(Notification notification);
}
