package kr.co.domain.auth.exception;

import kr.co.domain.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_TOKEN("token이 유효하지 않습니다.", 401),
    TOKEN_EXPIRED("token이 만료되었습니다.", 401),
    TOKEN_NOT_FOUND("사용자에 맞는 token를 찾을 수 없습니다.", 404),
    TOKEN_MISMATCH("제공된 token이 저장된 token과 일치하지 않습니다.", 403),
    UNAUTHORIZED_TOKEN_REQUEST("token 요청 권한이 없습니다.", 403);
    ;

    private final String message;
    private final int statusCode;
}
