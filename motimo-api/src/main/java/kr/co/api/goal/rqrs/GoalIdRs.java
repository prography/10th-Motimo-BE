package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record GoalIdRs(
        @Schema(description = "생성된 아이디")
        UUID id
) {

}
