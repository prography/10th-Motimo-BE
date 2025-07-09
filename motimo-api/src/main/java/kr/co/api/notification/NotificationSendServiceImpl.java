package kr.co.api.notification;

import kr.co.api.notification.dto.NotificationSendDto;
import kr.co.api.notification.util.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationSendServiceImpl implements NotificationSendService {
    private final NotificationSender notificationSender;

    public void send(NotificationSendDto dto) {

    }
}
