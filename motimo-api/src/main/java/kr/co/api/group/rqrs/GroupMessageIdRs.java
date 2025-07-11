package kr.co.api.group.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record GroupMessageIdRs(
        @Schema(description = "영향을 받은 그룹 채팅 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id
) {

}
