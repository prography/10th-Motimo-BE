package kr.co.api.group.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record GroupJoinRq(
        @Schema(description = "그룹에 가입할 목표 아이디")
        UUID goalId
) {

}
