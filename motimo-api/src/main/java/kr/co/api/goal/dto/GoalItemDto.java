package kr.co.api.goal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.goal.Goal;

public record GoalItemDto(
        UUID id,
        String title,
        LocalDate dueDate,
        float progress,
        LocalDateTime createdAt
) {

    public static GoalItemDto from(Goal goal) {
        return new GoalItemDto(
                goal.getId(),
                goal.getTitle(),
                goal.getDueDateValue(),
                goal.calculateProgress(),
                goal.getCreatedAt()
        );
    }
}
