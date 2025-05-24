package kr.co.domain.auth.exception;

import kr.co.domain.common.exception.BusinessException;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException() {
        super(AuthErrorCode.INVALID_TOKEN);
    }
}

