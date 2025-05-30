package kr.co.infra.rdb.todo.repository;

import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.exception.TodoNotFoundException;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.infra.rdb.todo.entity.TodoEntity;
import kr.co.infra.rdb.todo.util.TodoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
    public CustomSlice<Todo> findAllBySubGoalId(UUID subGoalId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDate today = LocalDate.now();
        Slice<TodoEntity> todoEntities = todoJpaRepository.findAllBySubGoalIdForTodayAndIncomplete(
                subGoalId, today, pageable);
        List<Todo> todos = todoEntities.getContent().stream()
                .map(TodoMapper::toDomain)
                .toList();
        return new CustomSlice<>(todos, todoEntities.hasNext());
    }

    @Override
    public CustomSlice<Todo> findAllByUserId(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<TodoEntity> todoEntities = todoJpaRepository.findAllByAuthorId(userId, pageable);
        List<Todo> todos = todoEntities.getContent().stream()
                .map(TodoMapper::toDomain)
                .toList();
        return new CustomSlice<>(todos, todoEntities.hasNext());
    }

    @Override
    public void deleteById(UUID id) {
        todoJpaRepository.deleteById(id);
    }

}
