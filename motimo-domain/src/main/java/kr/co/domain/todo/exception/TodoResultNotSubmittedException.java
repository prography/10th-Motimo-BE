package kr.co.domain.todo.exception;

import kr.co.domain.common.exception.BusinessException;

public class TodoResultNotSubmittedException extends BusinessException {

    public TodoResultNotSubmittedException() {
        super(TodoErrorCode.TODO_RESULT_NOT_SUBMITTED);
    }
}
