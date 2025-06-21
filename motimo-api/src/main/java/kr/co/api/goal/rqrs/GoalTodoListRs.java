package kr.co.api.goal.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import kr.co.api.goal.dto.GoalTodoListDto;

public record GoalTodoListRs(
        @Schema(description = "목표 Id", example = "0197157f-aea4-77bb-8581-3213eb5bd2aq")
        UUID id,

        @Schema(description = "목표 이름", example = "자격증 따기")
        String title,

        @Schema(description = "목표 완료 날짜", type = "date")
        LocalDate dueDate,

        @Schema(description = "세부 목표 목록")
        List<SubGoalRs> subGoals
) {

        public static GoalTodoListRs from(GoalTodoListDto dto) {
                return new GoalTodoListRs(
                        dto.id(),
                        dto.title(),
                        dto.dueDate(),
                        dto.subGoals().stream().map(SubGoalRs::from).toList()
                );
        }
}
