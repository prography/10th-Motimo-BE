package kr.co.api.group.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record JoinedGroupRs(
        @Schema(description = "그룹 이름 (현재는 목표와 동일)", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,
        @Schema(description = "그룹 마지막 활동 날짜")
        LocalDateTime lastActiveDate,
        @Schema(description = "알림 활성화 여부", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean isNotificationActive
) {
}
