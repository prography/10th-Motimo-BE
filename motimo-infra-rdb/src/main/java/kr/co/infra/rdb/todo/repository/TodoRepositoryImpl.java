package kr.co.infra.rdb.todo.repository;

import static com.querydsl.core.types.ExpressionUtils.anyOf;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import kr.co.domain.goal.dto.GoalTodoCount;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoStatus;
import kr.co.domain.todo.dto.TodoItem;
import kr.co.domain.todo.dto.TodoResultItem;
import kr.co.domain.todo.exception.TodoNotFoundException;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.infra.rdb.goal.entity.QGoalEntity;
import kr.co.infra.rdb.subGoal.entity.QSubGoalEntity;
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
    public Todo create(Todo todo) {
        TodoEntity entity = TodoMapper.toEntity(todo);
        return TodoMapper.toDomain(todoJpaRepository.save(entity));
    }

    @Override
    public Todo findById(UUID id) {
        return todoJpaRepository.findById(id)
                .map(TodoMapper::toDomain)
                .orElseThrow(TodoNotFoundException::new);
    }

    @Override
    public List<TodoItem> findIncompleteOrDateTodosBySubGoalId(UUID subGoalId, LocalDate date) {
        QTodoEntity todoEntity = QTodoEntity.todoEntity;
        QTodoResultEntity todoResultEntity = QTodoResultEntity.todoResultEntity;

        return todoItemJPAQuery(todoEntity, todoResultEntity)
                .where(
                        todoEntity.subGoalId.eq(subGoalId)
                                .and(anyOf(
                                        todoEntity.status.ne(TodoStatus.COMPLETE),
                                        todoEntity.date.eq(date)
                                ))
                )
                .fetch();
    }


    @Override
    public List<TodoItem> findAllByUserId(UUID userId) {
        QTodoEntity todoEntity = QTodoEntity.todoEntity;
        QTodoResultEntity todoResultEntity = QTodoResultEntity.todoResultEntity;

        return todoItemJPAQuery(todoEntity, todoResultEntity)
                .where(todoEntity.userId.eq(userId))
                .fetch();
    }

    @Override
    public List<TodoItem> findAllBySubGoalId(UUID subGoalId) {
        QTodoEntity todoEntity = QTodoEntity.todoEntity;
        QTodoResultEntity todoResultEntity = QTodoResultEntity.todoResultEntity;

        return todoItemJPAQuery(todoEntity, todoResultEntity)
                .where(todoEntity.subGoalId.eq(subGoalId))
                .fetch();

    }

    @Override
    public boolean existsById(UUID id) {
        return todoJpaRepository.existsById(id);
    }

    @Override
    public Todo update(Todo todo) {
        TodoEntity todoEntity = todoJpaRepository.findById(todo.getId())
                .orElseThrow(TodoNotFoundException::new);
        todoEntity.update(todo.getTitle(), todo.getDate(), todo.getStatus());
        return TodoMapper.toDomain(todoJpaRepository.save(todoEntity));
    }

    @Override
    public void deleteById(UUID id) {
        todoJpaRepository.deleteById(id);
    }

    @Override
    public List<GoalTodoCount> countTodosByGoalIds(List<UUID> goalIds) {

        QGoalEntity goalEntity = QGoalEntity.goalEntity;
        QSubGoalEntity subGoalEntity = QSubGoalEntity.subGoalEntity;
        QTodoEntity todoEntity = QTodoEntity.todoEntity;
        QTodoResultEntity todoResultEntity = QTodoResultEntity.todoResultEntity;

        return jpaQueryFactory
                .select(Projections.constructor(GoalTodoCount.class,
                        goalEntity.id,
                        todoEntity.id.countDistinct(),
                        todoResultEntity.id.countDistinct()))
                .from(goalEntity)
                .join(subGoalEntity).on(subGoalEntity.goal.eq(goalEntity))
                .join(todoEntity).on(todoEntity.subGoalId.eq(subGoalEntity.id))
                .leftJoin(todoResultEntity).on(todoResultEntity.todoId.eq(todoEntity.id))
                .where(goalEntity.id.in(goalIds))
                .groupBy(goalEntity.id)
                .fetch();
    }

    private NumberExpression<Integer> getPriorityOrder(QTodoEntity todoEntity,
            QTodoResultEntity todoResultEntity) {
        return new CaseBuilder()
                .when(todoEntity.status.eq(TodoStatus.INCOMPLETE)).then(0)
                .when(todoEntity.status.eq(TodoStatus.COMPLETE).and(todoResultEntity.id.isNull()))
                .then(1)
                .otherwise(2);
    }

    private JPAQuery<TodoItem> todoItemJPAQuery(QTodoEntity todoEntity,
            QTodoResultEntity todoResultEntity) {

        ConstructorExpression<TodoResultItem> resultItem =
                Projections.constructor(TodoResultItem.class,
                        todoResultEntity.id,
                        todoResultEntity.emotion,
                        todoResultEntity.content,
                        todoResultEntity.filePath);

        ConstructorExpression<TodoItem> todoItem =
                Projections.constructor(TodoItem.class,
                        todoEntity.id,
                        todoEntity.title,
                        todoEntity.date,
                        todoEntity.status,
                        todoEntity.createdAt,
                        resultItem);

        return jpaQueryFactory
                .select(todoItem)
                .from(todoEntity)
                .leftJoin(todoResultEntity).on(todoResultEntity.todoId.eq(todoEntity.id))
                .orderBy(
                        todoEntity.date.asc().nullsLast(),
                        todoEntity.createdAt.asc()
                );
    }
}
