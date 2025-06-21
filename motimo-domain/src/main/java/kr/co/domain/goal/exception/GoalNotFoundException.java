package kr.co.domain.goal.exception;

import kr.co.domain.common.exception.BusinessException;

public class GoalNotFoundException extends BusinessException {

    public GoalNotFoundException() {
        super(GoalErrorCode.GOAL_NOT_FOUND);
    }
}
