package kr.co.api.notification;

import java.util.UUID;
import kr.co.api.notification.docs.NotificationControllerSwagger;
import kr.co.api.notification.dto.NotificationItemDto;
import kr.co.api.notification.rqrs.NotificationItemRs;
import kr.co.api.notification.service.NotificationQueryService;
import kr.co.api.security.annotation.AuthUser;
import kr.co.domain.common.pagination.CustomPage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/notifications")
public class NotificationController implements NotificationControllerSwagger {

    private final NotificationQueryService notificationQueryService;

    public NotificationController(final NotificationQueryService notificationQueryService) {
        this.notificationQueryService = notificationQueryService;
    }

    @GetMapping
    public CustomPage<NotificationItemRs> getNotificationList(@AuthUser UUID userId, @RequestParam(defaultValue = "0") int page, @RequestParam int size) {
        CustomPage<NotificationItemDto> dtos = notificationQueryService.getNotificationList(userId, page, size);

        return dtos.map(NotificationItemRs::from);
    }

}
