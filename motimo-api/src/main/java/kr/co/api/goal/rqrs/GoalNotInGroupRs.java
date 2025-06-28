package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;

public record GoalNotInGroupRs(
        @Schema(description = "목표 제목")
        String title
) {

}
