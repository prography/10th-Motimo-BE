package kr.co.api.notification.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.co.api.notification.dto.NotificationItemDto;
import kr.co.domain.notification.NotificationType;

public record NotificationItemRs(
        @Schema(description = "알림 아이디입니다.")
        UUID id,
        @Schema(description = "알림 내용 전체입니다.")
        String content,
        @Schema(description = "알림 타입입니다.")
        NotificationType type,
        @Schema(description = "알림과 연결되는 항목의 아이디입니다.")
        UUID referenceId,
        @Schema(description = "읽음 여부입니다.")
        boolean isRead

) {
    public static NotificationItemRs from(NotificationItemDto dto) {
        return new NotificationItemRs(
                dto.notificationId(),
                dto.content(),
                dto.type(),
                dto.referenceId(),
                dto.isRead()
        );
    }
}
