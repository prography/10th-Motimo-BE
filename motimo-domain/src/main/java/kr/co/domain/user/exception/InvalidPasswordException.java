package kr.co.domain.user.exception;

import kr.co.domain.common.exception.BusinessException;

public class InvalidPasswordException extends BusinessException {

    public InvalidPasswordException() {
        super(UserErrorCode.INVALID_PASSWORD);
    }
}
