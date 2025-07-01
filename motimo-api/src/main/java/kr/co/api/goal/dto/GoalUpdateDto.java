package kr.co.api.goal.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import kr.co.api.goal.rqrs.GoalUpdateRq;

public record GoalUpdateDto(
        String title,

        boolean isPeriodByMonth,

        Integer month,

        LocalDate dueDate,

        List<SubGoalUpdateDto> subGoals,

        Set<UUID> deletedSubGoalIds
) {
    public static GoalUpdateDto from(GoalUpdateRq rq) {
        return new GoalUpdateDto(
                rq.title(),
                rq.isPeriodByMonth(),
                rq.month(),
                rq.dueDate(),
                rq.subGoals().stream().map(SubGoalUpdateDto::from).toList(),
                rq.deletedSubGoalIds()
        );
    }
}
