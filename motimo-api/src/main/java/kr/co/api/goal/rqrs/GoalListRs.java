package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GoalListRs(
    @Schema(description = "목표 목록")
    List<GoalItemRs> goals
) {
}
