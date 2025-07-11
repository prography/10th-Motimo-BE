package kr.co.domain.todo.exception;

import kr.co.domain.common.exception.BusinessException;

public class TodoNotCompleteException extends BusinessException {

    public TodoNotCompleteException() {
        super(TodoErrorCode.TODO_NOT_COMPLETE);
    }
}
