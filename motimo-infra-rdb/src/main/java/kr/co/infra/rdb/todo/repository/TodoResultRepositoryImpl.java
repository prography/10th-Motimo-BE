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
    public TodoResult create(TodoResult todoResult) {
        TodoResultEntity entity = TodoResultMapper.toEntity(todoResult);
        return TodoResultMapper.toDomain(todoResultJpaRepository.save(entity));
    }

    @Override
    public TodoResult update(TodoResult todoResult) {
        TodoResultEntity entity = todoResultJpaRepository.findById(todoResult.getId())
                .orElseThrow(TodoResultNotSubmittedException::new);

        entity.update(todoResult.getEmotion(), todoResult.getContent(), todoResult.getFilePath());
        return TodoResultMapper.toDomain(todoResultJpaRepository.save(entity));
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
