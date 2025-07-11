package kr.co.domain.group.exception;

import kr.co.domain.common.exception.BusinessException;

public class MessageContentLoadingException extends BusinessException {

    public MessageContentLoadingException() {
        super(GroupErrorCode.MESSAGE_LOAD_FAIL);
    }
}
