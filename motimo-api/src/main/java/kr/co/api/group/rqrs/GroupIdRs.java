package kr.co.api.group.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record GroupIdRs(
        @Schema(description = "가입된 그룹 아이디")
        UUID id
) {

}
