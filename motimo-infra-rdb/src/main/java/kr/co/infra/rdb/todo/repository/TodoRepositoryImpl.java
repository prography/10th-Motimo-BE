package kr.co.infra.rdb.todo.repository;

import kr.co.domain.todo.Todo;
import kr.co.domain.todo.exception.TodoNotFoundException;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.infra.rdb.todo.entity.TodoEntity;
import kr.co.infra.rdb.todo.util.TodoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepository {
    private final TodoJpaRepository todoJpaRepository;

    @Override
    public Todo save(Todo toDo) {
        TodoEntity entity = TodoMapper.toEntity(toDo);
        TodoEntity saved = todoJpaRepository.save(entity);
        return TodoMapper.toDomain(saved);
    }

    @Override
    public Todo findById(UUID id) {
        return todoJpaRepository.findById(id)
                .map(TodoMapper::toDomain)
                .orElseThrow(TodoNotFoundException::new);
    }

    @Override
    public List<Todo> findAllBySubGoalId(UUID subGoalId) {
        return todoJpaRepository.findAllBySubGoalId(subGoalId).stream()
                .map(TodoMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        todoJpaRepository.deleteById(id);
    }
}
