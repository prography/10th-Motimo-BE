package kr.co.domain.group.exception;

import kr.co.domain.common.exception.BusinessException;
import kr.co.domain.user.exception.UserErrorCode;

public class MessageNotFoundException extends BusinessException {

    public MessageNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND);
    }
}
