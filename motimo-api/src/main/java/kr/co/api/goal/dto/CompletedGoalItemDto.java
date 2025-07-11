package kr.co.api.goal.dto;

import java.time.LocalDate;
import java.util.UUID;
import kr.co.domain.goal.Goal;

public record CompletedGoalItemDto(
        UUID id,
        String title,
        LocalDate dueDate,
        long todoCount,
        long todoResultCount
) {

    public static CompletedGoalItemDto of(Goal goal, long todoCount, long todoResultCount) {
        return new CompletedGoalItemDto(
                goal.getId(),
                goal.getTitle(),
                goal.getDueDateValue(),
                todoCount,
                todoResultCount
        );
    }
}
