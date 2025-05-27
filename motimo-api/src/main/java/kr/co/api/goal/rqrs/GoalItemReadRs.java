package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GoalItemReadRs(
    @Schema(description = "목표 이름", example = "자격증 따기")
    String title,

    @Schema(description = "목표 완료 날짜", type = "date")
    LocalDate dueDate,

    @Schema(description = "목표 달성률 (%)", example = "24.5")
    float progress
) {
}
