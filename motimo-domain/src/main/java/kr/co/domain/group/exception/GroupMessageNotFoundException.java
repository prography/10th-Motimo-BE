package kr.co.domain.group.exception;

import kr.co.domain.common.exception.BusinessException;

public class GroupMessageNotFoundException extends BusinessException {

    public GroupMessageNotFoundException() {
        super(GroupErrorCode.GROUP_MESSAGE_NOT_FOUND);
    }
}
