package kr.co.domain.group.exception;

import kr.co.domain.common.exception.BusinessException;

public class AlreadyJoinedGroupException extends BusinessException {

    public AlreadyJoinedGroupException() {
        super(GroupErrorCode.ALREADY_JOINED_GROUP);
    }
}
