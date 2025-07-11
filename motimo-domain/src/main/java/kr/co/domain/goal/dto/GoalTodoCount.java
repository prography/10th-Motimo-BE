package kr.co.domain.goal.dto;

import java.util.UUID;

public record GoalTodoCount(
        UUID goalId,
        long todoCount,
        long todoResultCount
) {

}
