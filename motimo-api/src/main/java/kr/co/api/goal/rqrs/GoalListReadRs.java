package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GoalListReadRs(
    @Schema(description = "목표 목록")
    List<GoalItemReadRs> goals
) {
}
