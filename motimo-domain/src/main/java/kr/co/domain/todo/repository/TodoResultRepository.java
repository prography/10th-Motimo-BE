package kr.co.domain.todo.repository;

import java.util.UUID;
import kr.co.domain.todo.TodoResult;

public interface TodoResultRepository {

    TodoResult save(TodoResult todo);

    TodoResult findById(UUID id);

    TodoResult findByTodoId(UUID todoId);

    void deleteById(UUID id);
}
