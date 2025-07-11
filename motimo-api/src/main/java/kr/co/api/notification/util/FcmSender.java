package kr.co.api.notification.util;

import kr.co.api.notification.dto.NotificationSendDto;
import org.springframework.stereotype.Component;

@Component
class FcmSender implements NotificationSender {
  public void send(NotificationSendDto notification) {
    // FCM SDK 호출
  }
}