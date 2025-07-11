package kr.co.api.group.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record GroupMemberRs(
        @Schema(description = "사용자 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID userId,
        @Schema(description = "사용자 닉네임", requiredMode = Schema.RequiredMode.REQUIRED)
        String nickname,
        @Schema(description = "마지막 접속일", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime lastOnlineDate,
        @Schema(description = "찌르기 활성화 여부", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean isActivePoke
) {

}
