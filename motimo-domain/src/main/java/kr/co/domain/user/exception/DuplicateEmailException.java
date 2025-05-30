package kr.co.domain.user.exception;

import kr.co.domain.common.exception.BusinessException;

public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException() {
        super(UserErrorCode.EMAIL_ALREADY_EXISTS);
    }
}
