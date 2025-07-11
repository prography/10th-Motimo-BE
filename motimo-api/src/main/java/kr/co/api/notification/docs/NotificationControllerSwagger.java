package kr.co.api.notification.docs;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import kr.co.api.notification.rqrs.NotificationItemRs;
import kr.co.domain.common.pagination.CustomSlice;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "알림 API", description = "알림 관련 API 목록입니다")
public interface NotificationControllerSwagger {
    @Operation(summary = "알림 목록 API", description = "알림 목록을 조회합니다.")
    CustomSlice<NotificationItemRs> getNotificationList(UUID userId, @RequestParam int offset, @RequestParam int limit);
}
