package kr.co.api.goal.dto;

import java.util.UUID;
import kr.co.api.goal.rqrs.SubGoalUpdateRq;

public record SubGoalUpdateDto(
        UUID updateId,

        String title,

        int order
) {
        public static SubGoalUpdateDto from(SubGoalUpdateRq rq) {
                return new SubGoalUpdateDto(rq.id(), rq.title(), rq.order());
        }
}
