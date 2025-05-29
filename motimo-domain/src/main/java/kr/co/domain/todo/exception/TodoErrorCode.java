package kr.co.domain.todo.exception;

import kr.co.domain.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TodoErrorCode implements ErrorCode {
    TODO_NOT_FOUND("todo를 찾을 수 없습니다.", 404),
    ;

    private final String message;
    private final int statusCode;
}
