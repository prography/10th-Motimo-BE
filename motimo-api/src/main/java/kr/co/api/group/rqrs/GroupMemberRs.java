package kr.co.api.group.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.api.group.service.dto.GroupMemberDto;

public record GroupMemberRs(
        @Schema(description = "사용자 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID userId,
        @Schema(description = "사용자 닉네임", requiredMode = Schema.RequiredMode.REQUIRED)
        String nickname,
        @Schema(description = "마지막 접속일", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime lastOnlineDate,
        @Schema(description = "찌르기 활성화 여부, 본인이면 null", requiredMode = Schema.RequiredMode.REQUIRED)
        Boolean isActivePoke
) {

    public static GroupMemberRs from(GroupMemberDto dto) {
        return new GroupMemberRs(
                dto.memberId(),
                dto.nickname(),
                dto.lastOnlineDate(),
                dto.isActivePoke()
        );
    }
}
