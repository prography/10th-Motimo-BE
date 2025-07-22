package kr.co.domain.group.exception;

import kr.co.domain.common.exception.BusinessException;

public class MessageNotFoundException extends BusinessException {

    public MessageNotFoundException() {
        super(GroupErrorCode.MESSAGE_NOT_FOUND);
    }
}
