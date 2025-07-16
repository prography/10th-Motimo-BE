package kr.co.api.goal.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import kr.co.domain.goal.Goal;

public record GoalWithSubGoalTodoDto(
        UUID id,
        String title,
        LocalDate dueDate,
        List<SubGoalWithTodosDto> subGoals
) {

    public static GoalWithSubGoalTodoDto of(Goal goal, List<SubGoalWithTodosDto> subGoals) {
        return new GoalWithSubGoalTodoDto(
                goal.getId(),
                goal.getTitle(),
                goal.getDueDateValue(),
                subGoals
        );
    }
}
