package kr.co.infra.rdb.todo.repository;

import static com.querydsl.core.types.ExpressionUtils.anyOf;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoStatus;
import kr.co.domain.todo.dto.TodoSummary;
import kr.co.domain.todo.exception.TodoNotFoundException;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.infra.rdb.todo.entity.QTodoEntity;
import kr.co.infra.rdb.todo.entity.QTodoResultEntity;
import kr.co.infra.rdb.todo.entity.TodoEntity;
import kr.co.infra.rdb.todo.util.TodoMapper;
import lombok.RequiredArgsConstructor;
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
    public List<TodoSummary> findIncompleteOrDateTodosBySubGoalId(UUID subGoalId, LocalDate date) {
        QTodoEntity todoEntity = QTodoEntity.todoEntity;
        QTodoResultEntity todoResultEntity = QTodoResultEntity.todoResultEntity;
        NumberExpression<Integer> priorityOrder = getPriorityOrder(todoEntity, todoResultEntity);

        return jpaQueryFactory
                .select(Projections.constructor(TodoSummary.class,
                        todoEntity.id,
                        todoEntity.title,
                        todoEntity.date,
                        todoEntity.status,
                        todoEntity.createdAt,
                        todoResultEntity.id.isNotNull()
                ))
                .from(todoEntity)
                .leftJoin(todoResultEntity).on(todoResultEntity.todoId.eq(todoEntity.id))
                .where(
                        todoEntity.subGoalId.eq(subGoalId)
                                .and(anyOf(
                                        todoEntity.status.ne(TodoStatus.COMPLETE),
                                        todoEntity.date.eq(date)
                                ))
                )
                .orderBy(
                        priorityOrder.asc(),
                        todoEntity.date.asc().nullsLast(),
                        todoEntity.createdAt.asc()
                )
                .fetch();
    }

    @Override
    public List<TodoSummary> findAllByUserId(UUID userId) {
        QTodoEntity todoEntity = QTodoEntity.todoEntity;
        QTodoResultEntity todoResultEntity = QTodoResultEntity.todoResultEntity;

        NumberExpression<Integer> priorityOrder = getPriorityOrder(todoEntity, todoResultEntity);

        return jpaQueryFactory
                .select(Projections.constructor(TodoSummary.class,
                        todoEntity.id,
                        todoEntity.title,
                        todoEntity.date,
                        todoEntity.status,
                        todoEntity.createdAt,
                        todoResultEntity.id.isNotNull()
                ))
                .from(todoEntity)
                .leftJoin(todoResultEntity).on(todoResultEntity.todoId.eq(todoEntity.id))
                .where(todoEntity.userId.eq(userId))
                .orderBy(
                        priorityOrder.asc(),
                        todoEntity.date.asc().nullsLast()
                )
                .fetch();
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
                .when(todoEntity.status.eq(TodoStatus.INCOMPLETE)).then(0)
                .when(todoEntity.status.eq(TodoStatus.COMPLETE).and(todoResultEntity.id.isNull()))
                .then(1)
                .otherwise(2);
    }
}
