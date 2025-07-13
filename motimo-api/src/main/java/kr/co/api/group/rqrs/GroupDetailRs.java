package kr.co.api.group.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kr.co.api.group.service.dto.GroupDto;

public record GroupDetailRs(
        @Schema(description = "그룹 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID groupId,
        @Schema(description = "그룹 이름 (현재는 목표와 동일)", requiredMode = Schema.RequiredMode.REQUIRED)
        String name
) {
    public static GroupDetailRs from(GroupDto dto) {
        return new GroupDetailRs(dto.groupId(), dto.name());
    }
}
