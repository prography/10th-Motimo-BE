package kr.co.api.notification.service;

import kr.co.domain.notification.Notification;

public interface NotificationCommandService {
    void save(Notification notification);
}
