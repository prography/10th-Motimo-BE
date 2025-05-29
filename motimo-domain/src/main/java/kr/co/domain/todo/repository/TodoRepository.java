package kr.co.domain.todo.repository;

import kr.co.domain.todo.Todo;

import java.util.List;
import java.util.UUID;

public interface TodoRepository {
    Todo save(Todo toDo);
    Todo findById(UUID id);
    List<Todo> findAllBySubGoalId(UUID subGoalId);
    void deleteById(UUID id);
}
