package kr.co.domain.goal.exception;

import kr.co.domain.common.exception.BusinessException;

public class GoalCompleteFailedException extends BusinessException {

    public GoalCompleteFailedException() {
        super(GoalErrorCode.GOAL_COMPLETION_CONDITION_NOT_MATCHED);
    }
}
