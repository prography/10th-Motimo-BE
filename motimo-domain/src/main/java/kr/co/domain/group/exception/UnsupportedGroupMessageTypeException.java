package kr.co.domain.group.exception;

import kr.co.domain.common.exception.BusinessException;

public class UnsupportedGroupMessageTypeException extends BusinessException {

    public UnsupportedGroupMessageTypeException() {
        super(GroupErrorCode.UNSUPPORTED_MESSAGE_TYPE);
    }
}
