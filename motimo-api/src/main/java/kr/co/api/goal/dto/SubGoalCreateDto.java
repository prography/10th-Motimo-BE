package kr.co.api.goal.dto;

import kr.co.api.goal.rqrs.SubGoalCreateRq;

public record SubGoalCreateDto(
        String title
) {
        public static SubGoalCreateDto from(SubGoalCreateRq rq) {
                return new SubGoalCreateDto(rq.title());
        }
}
