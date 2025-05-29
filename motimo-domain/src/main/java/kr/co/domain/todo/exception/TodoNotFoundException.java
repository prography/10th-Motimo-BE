package kr.co.domain.todo.exception;

import kr.co.domain.common.exception.BusinessException;

public class TodoNotFoundException extends BusinessException {
    public TodoNotFoundException() {
        super(TodoErrorCode.TODO_NOT_FOUND);
    }
}
