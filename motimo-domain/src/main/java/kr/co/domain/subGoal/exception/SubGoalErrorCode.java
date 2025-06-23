package kr.co.domain.subGoal.exception;

import kr.co.domain.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubGoalErrorCode implements ErrorCode {
    SUB_GOAL_NOT_FOUND("세부 목표를 찾을 수 없습니다.", 404),
    SUB_GOAL_ACCESS_DENIED("해당 세부 목표에 대한 권한이 없습니다.", 403),
    ;

    private final String message;
    private final int statusCode;
}
