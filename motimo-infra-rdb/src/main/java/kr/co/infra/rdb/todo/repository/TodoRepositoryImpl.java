package kr.co.infra.rdb.todo.repository;

import static com.querydsl.core.types.ExpressionUtils.anyOf;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.dto.TodoSummary;
import kr.co.domain.todo.exception.TodoNotFoundException;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.infra.rdb.todo.entity.QTodoEntity;
import kr.co.infra.rdb.todo.entity.QTodoResultEntity;
import kr.co.infra.rdb.todo.entity.TodoEntity;
import kr.co.infra.rdb.todo.util.TodoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepository {

    private final TodoJpaRepository todoJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Todo save(Todo todo) {
        TodoEntity entity = TodoMapper.toEntity(todo);
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
    public CustomSlice<TodoSummary> findAllBySubGoalId(UUID subGoalId, int page, int size) {

        QTodoEntity todoEntity = QTodoEntity.todoEntity;
        QTodoResultEntity todoResultEntity = QTodoResultEntity.todoResultEntity;
        Pageable pageable = PageRequest.of(page, size);
        LocalDate today = LocalDate.now();
        NumberExpression<Integer> priorityOrder = getPriorityOrder(todoEntity, todoResultEntity);

        List<TodoSummary> results = jpaQueryFactory
                .select(Projections.constructor(TodoSummary.class,
                        todoEntity.id,
                        todoEntity.title,
                        todoEntity.date,
                        todoEntity.completed,
                        todoEntity.createdAt,
                        todoResultEntity.id.isNotNull()
                ))
                .from(todoEntity)
                .leftJoin(todoResultEntity).on(todoResultEntity.todoId.eq(todoEntity.id))
                .where(
                        todoEntity.subGoalId.eq(subGoalId)
                                .and(anyOf(
                                        todoEntity.completed.eq(false),
                                        todoEntity.date.eq(today)
                                ))
                )
                .orderBy(
                        priorityOrder.asc(),
                        todoEntity.date.asc().nullsLast()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();
        List<TodoSummary> paged = results.stream()
                .limit(pageable.getPageSize())
                .toList();

        return new CustomSlice<>(paged, hasNext);
    }

    @Override
    public CustomSlice<TodoSummary> findAllByUserId(UUID userId, int page, int size) {
        QTodoEntity todoEntity = QTodoEntity.todoEntity;
        QTodoResultEntity todoResultEntity = QTodoResultEntity.todoResultEntity;

        Pageable pageable = PageRequest.of(page, size);
        NumberExpression<Integer> priorityOrder = getPriorityOrder(todoEntity, todoResultEntity);

        List<TodoSummary> results = jpaQueryFactory
                .select(Projections.constructor(TodoSummary.class,
                        todoEntity.id,
                        todoEntity.title,
                        todoEntity.date,
                        todoEntity.completed,
                        todoEntity.createdAt,
                        todoResultEntity.id.isNotNull()
                ))
                .from(todoEntity)
                .leftJoin(todoResultEntity).on(todoResultEntity.todoId.eq(todoEntity.id))
                .where(todoEntity.authorId.eq(userId))
                .orderBy(
                        priorityOrder.asc(),
                        todoEntity.date.asc().nullsLast()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();
        List<TodoSummary> paged = results.stream()
                .limit(pageable.getPageSize())
                .toList();

        return new CustomSlice<>(paged, hasNext);
    }

    @Override
    public boolean existsById(UUID id) {
        return todoJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        todoJpaRepository.deleteById(id);
    }


    private NumberExpression<Integer> getPriorityOrder(QTodoEntity todoEntity,
            QTodoResultEntity todoResultEntity) {
        return new CaseBuilder()
                .when(todoEntity.completed.eq(false)).then(0)
                .when(todoEntity.completed.eq(true).and(todoResultEntity.id.isNull())).then(1)
                .otherwise(2);
    }
}
