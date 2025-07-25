package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import kr.co.api.goal.dto.GoalWithSubGoalTodoDto;

public record GoalWithSubGoalTodoRs(
        @Schema(description = "목표 Id", example = "0197157f-aea4-77bb-8581-3213eb5bd2aq", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id,
        @Schema(description = "목표 이름", example = "자격증 따기", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,
        @Schema(description = "목표 완료 날짜", type = "date", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDate dueDate,
        @Schema(description = "투두를 포함한 세부 목표 목록")
        List<SubGoalWithTodosRs> subGoals
) {

    public static GoalWithSubGoalTodoRs from(GoalWithSubGoalTodoDto dto) {
        return new GoalWithSubGoalTodoRs(
                dto.id(),
                dto.title(),
                dto.dueDate(),
                dto.subGoals().stream().map(SubGoalWithTodosRs::from).toList()
        );
    }
}