package kr.co.domain.group.exception;

import kr.co.domain.common.exception.BusinessException;

public class AlreadyTodayPokeException extends BusinessException {

    public AlreadyTodayPokeException() {
        super(GroupErrorCode.ALREADY_TODAY_POKE);
    }
}
