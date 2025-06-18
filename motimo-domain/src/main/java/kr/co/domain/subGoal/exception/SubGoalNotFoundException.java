package kr.co.domain.subGoal.exception;

import kr.co.domain.common.exception.BusinessException;

public class SubGoalNotFoundException extends BusinessException {

    public SubGoalNotFoundException() {
        super(SubGoalErrorCode.SUB_GOAL_NOT_FOUND);
    }
}
