package kr.co.domain.todo.exception;

import kr.co.domain.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TodoErrorCode implements ErrorCode {
    TODO_NOT_FOUND("todo를 찾을 수 없습니다.", 404),
    TODO_RESULT_NOT_SUBMITTED("투두 결과가 아직 제출되지 않았습니다.", 404),
    TODO_ACCESS_DENIED("해당 TODO에 대한 권한이 없습니다.", 403),
    ;

    private final String message;
    private final int statusCode;
}
