package kr.co.domain.auth.exception;

import kr.co.domain.common.exception.BusinessException;

public class TokenNotFoundException extends BusinessException {
    public TokenNotFoundException() {
        super(AuthErrorCode.TOKEN_NOT_FOUND);
    }
}
