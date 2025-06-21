package kr.co.domain.todo.repository;

import java.util.Optional;
import java.util.UUID;
import kr.co.domain.todo.TodoResult;

public interface TodoResultRepository {

    TodoResult create(TodoResult todo);

    TodoResult findById(UUID id);

    Optional<TodoResult> findByTodoId(UUID todoId);

    TodoResult update(TodoResult todo);

    void deleteById(UUID id);
}
