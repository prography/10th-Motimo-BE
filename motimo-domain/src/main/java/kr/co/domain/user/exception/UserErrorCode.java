package kr.co.domain.user.exception;

import kr.co.domain.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", 404),
    EMAIL_ALREADY_EXISTS("이미 존재하는 이메일입니다.", 409),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다.", 401),
    UNSUPPORTED_PROVIDER_TYPE("지원하지 않는 Provider 타입입니다.", 400),
    ;

    private final String message;
    private final int statusCode;
}
