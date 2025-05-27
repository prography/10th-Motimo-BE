package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record GoalCreateRq(
    @NotNull
    @Size(min = 1, max = 20)
    @Schema(description = "목표 이름", example = "자격증 따기")
    String title,

    @NotNull
    @Schema(description = "개월 수로 기간 설정 여부")
    boolean isMonthSetting,

    @Size(min = 1, max = 12)
    @Schema(description = "목표 개월 수")
    Integer month,

    @Schema(description = "목표 완료 날짜", format = "date")
    LocalDate date,

    @Schema(description = "세부 목표 목록")
    List<SubGoalCreateRq> subGoals
) {
    public GoalCreateRq {
        if (Objects.isNull(subGoals)) {
            subGoals = new ArrayList<>();
        }
    }
}
