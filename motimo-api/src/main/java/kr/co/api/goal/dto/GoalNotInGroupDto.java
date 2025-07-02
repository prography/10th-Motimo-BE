package kr.co.api.goal.dto;

import java.time.LocalDate;
import java.util.UUID;
import kr.co.domain.goal.Goal;

public record GoalNotInGroupDto(
        UUID goalId,
        String title,
        LocalDate dueDate
) {

    public static GoalNotInGroupDto from(Goal goal) {
        return new GoalNotInGroupDto(
                goal.getId(),
                goal.getTitle(),
                goal.getDueDateValue()
        );
    }
}
