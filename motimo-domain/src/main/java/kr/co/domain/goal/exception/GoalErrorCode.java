package kr.co.domain.goal.exception;

import kr.co.domain.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GoalErrorCode implements ErrorCode {
    GOAL_NOT_FOUND("목표를 찾을 수 없습니다.", 404),
    GOAL_ACCESS_DENIED("해당 목표에 대한 권한이 없습니다.", 403),
    GOAL_COMPLETION_CONDITION_NOT_MATCHED("목표 완료 조건을 만족하지 않습니다.", 400)
    ;

    private final String message;
    private final int statusCode;
}
