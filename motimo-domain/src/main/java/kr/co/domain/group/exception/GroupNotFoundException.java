package kr.co.domain.group.exception;

import kr.co.domain.common.exception.BusinessException;

public class GroupNotFoundException extends BusinessException {

    public GroupNotFoundException() {
        super(GroupErrorCode.GROUP_NOT_FOUND);
    }
}
