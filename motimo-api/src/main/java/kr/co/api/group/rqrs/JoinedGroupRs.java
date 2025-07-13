package kr.co.api.group.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.api.group.service.dto.JoinedGroupDto;

public record JoinedGroupRs(
        @Schema(description = "그룹 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID groupId,
        @Schema(description = "그룹 이름 (현재는 목표와 동일)", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,
        @Schema(description = "그룹 마지막 활동 날짜")
        LocalDateTime lastActiveDate,
        @Schema(description = "알림 활성화 여부 - 현재는 true 고정(미개발)", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean isNotificationActive
) {

    public static JoinedGroupRs from(JoinedGroupDto dto) {
        return new JoinedGroupRs(
                dto.groupId(),
                dto.title(),
                dto.lastActiveDate(),
                dto.isNotificationAction()
        );
    }
}
