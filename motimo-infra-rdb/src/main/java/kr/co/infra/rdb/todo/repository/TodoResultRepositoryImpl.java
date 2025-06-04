package kr.co.infra.rdb.todo.repository;

import java.util.Optional;
import java.util.UUID;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.todo.exception.TodoResultNotSubmittedException;
import kr.co.domain.todo.repository.TodoResultRepository;
import kr.co.infra.rdb.todo.entity.TodoResultEntity;
import kr.co.infra.rdb.todo.util.TodoResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoResultRepositoryImpl implements TodoResultRepository {

    private final TodoResultJpaRepository todoResultJpaRepository;

    @Override
    public TodoResult save(TodoResult todo) {
        TodoResultEntity entity = TodoResultMapper.toEntity(todo);
        todoResultJpaRepository.save(entity);
        return TodoResultMapper.toDomain(entity);
    }

    @Override
    public TodoResult findById(UUID id) {
        return todoResultJpaRepository.findById(id)
                .map(TodoResultMapper::toDomain)
                .orElseThrow(TodoResultNotSubmittedException::new);
    }

    @Override
    public Optional<TodoResult> findByTodoId(UUID todoId) {
        return todoResultJpaRepository.findByTodoId(todoId)
                .map(TodoResultMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        todoResultJpaRepository.deleteById(id);
    }
}
