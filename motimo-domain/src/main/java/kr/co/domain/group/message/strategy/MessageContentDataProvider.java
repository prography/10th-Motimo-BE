package kr.co.domain.group.message.strategy;

import java.util.UUID;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.user.model.User;

public interface MessageContentDataProvider {

    Todo getTodo(UUID todoId);

    TodoResult getTodoResult(UUID todoResultId);

    User getUser(UUID userId);
}
