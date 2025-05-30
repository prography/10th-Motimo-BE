package kr.co.domain.auth.exception;

import kr.co.domain.common.exception.BusinessException;

public class TokenMismatchException extends BusinessException {

    public TokenMismatchException() {
        super(AuthErrorCode.TOKEN_MISMATCH);
    }
}

