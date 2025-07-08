package kr.co.domain.group.exception;

import kr.co.domain.common.exception.BusinessException;

public class UserNotInGroupException extends BusinessException {

    public UserNotInGroupException() {
        super(GroupErrorCode.USER_NOT_IN_GROUP);
    }
}
