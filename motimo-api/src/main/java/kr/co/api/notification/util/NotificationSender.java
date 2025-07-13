package kr.co.api.notification.util;

import kr.co.api.notification.dto.NotificationSendDto;

public interface NotificationSender {
  void send(NotificationSendDto notification);
}