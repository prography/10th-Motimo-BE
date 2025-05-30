package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record GoalUpdateRq(
        @NotNull
        @Size(min = 1, max = 20)
        @Schema(description = "목표 이름", example = "자격증 따기")
        String title,

        @NotNull
        @Schema(description = "목표 완료 날짜", format = "date")
        LocalDate dueDate,

        @Schema(description = "세부 목표 목록")
        List<SubGoalUpdateRq> subGoals
) {

    public GoalUpdateRq {
        if (Objects.isNull(subGoals)) {
            subGoals = new ArrayList<>();
        }
    }
}
