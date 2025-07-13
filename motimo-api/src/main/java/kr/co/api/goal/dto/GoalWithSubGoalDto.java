package kr.co.api.goal.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import kr.co.domain.goal.Goal;

public record GoalWithSubGoalDto(
        UUID id,
        String title,
        LocalDate dueDate,
        List<SubGoalDto> subGoals
) {

    public static GoalWithSubGoalDto of(Goal goal, List<SubGoalDto> subGoals) {
        return new GoalWithSubGoalDto(
                goal.getId(),
                goal.getTitle(),
                goal.getDueDateValue(),
                subGoals
        );
    }
}
