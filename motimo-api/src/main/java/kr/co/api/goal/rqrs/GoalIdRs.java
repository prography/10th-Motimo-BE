package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;

public record GoalIdRs(
    @Schema(description = "생성된 아이디")
    String id
) {
}
