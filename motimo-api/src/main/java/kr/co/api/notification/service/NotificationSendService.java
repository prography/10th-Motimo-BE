package kr.co.api.notification.service;

import kr.co.api.notification.dto.NotificationSendDto;

public interface NotificationSendService {
    void send(NotificationSendDto dto);
}
