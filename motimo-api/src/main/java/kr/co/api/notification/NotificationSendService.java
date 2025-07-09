package kr.co.api.notification;

import kr.co.api.notification.dto.NotificationSendDto;

public interface NotificationSendService {
    void send(NotificationSendDto dto);
}
