package kr.co.api.notification.rqrs;

import kr.co.api.notification.dto.NotificationItemDto;

public record NotificationItemRs(


) {
    public static NotificationItemRs from(NotificationItemDto dto) {
        return new NotificationItemRs();
    }
}
