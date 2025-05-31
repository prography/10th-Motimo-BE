package kr.co.api.goal.rqrs;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record GoalUpdateRq(
        @NotNull
        @Size(min = 1, max = 20)
        @Schema(description = "목표 이름", example = "자격증 따기")
        String title,

        @NotNull
        @Schema(description = "목표 완료 날짜", format = "date")
        LocalDate dueDate,

        @JsonSetter(nulls = Nulls.AS_EMPTY)
        @Schema(description = "세부 목표 목록")
        List<SubGoalUpdateRq> subGoals
) {

}
