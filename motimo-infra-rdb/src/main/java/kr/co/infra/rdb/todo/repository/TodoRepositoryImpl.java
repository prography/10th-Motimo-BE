package kr.co.infra.rdb.todo.repository;

import static com.querydsl.core.types.ExpressionUtils.anyOf;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.goal.dto.GoalTodoCount;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoStatus;
import kr.co.domain.todo.dto.TodoItemDto;
import kr.co.domain.todo.dto.TodoResultItemDto;
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
    public CustomSlice<TodoItemDto> findIncompleteOrDateTodosBySubGoalId(UUID subGoalId,
            LocalDate date, int offset, int size) {
        QTodoEntity todoEntity = QTodoEntity.todoEntity;
        QTodoResultEntity todoResultEntity = QTodoResultEntity.todoResultEntity;

        List<TodoItemDto> results = todoItemJPAQuery(todoEntity, todoResultEntity)
                .where(
                        todoEntity.subGoalId.eq(subGoalId)
                                .and(anyOf(
                                        todoEntity.status.ne(TodoStatus.COMPLETE),
                                        todoEntity.date.eq(date)
                                ))
                )
                .offset(offset)
                .limit(size + 1)
                .fetch();

        boolean hasNext = results.size() > size;
        if (hasNext) {
            results = results.subList(0, size);
        }

        return new CustomSlice<>(results, hasNext, offset, size);
    }

    @Override
    public List<TodoItemDto> findAllByUserId(UUID userId) {
        QTodoEntity todoEntity = QTodoEntity.todoEntity;
        QTodoResultEntity todoResultEntity = QTodoResultEntity.todoResultEntity;

        return todoItemJPAQuery(todoEntity, todoResultEntity)
                .where(todoEntity.userId.eq(userId))
                .fetch();
    }

    @Override
    public List<TodoItemDto> findAllBySubGoalId(UUID subGoalId) {
        QTodoEntity todoEntity = QTodoEntity.todoEntity;
        QTodoResultEntity todoResultEntity = QTodoResultEntity.todoResultEntity;

        return todoItemJPAQuery(todoEntity, todoResultEntity)
                .where(todoEntity.subGoalId.eq(subGoalId))
                .fetch();

    }

    @Override
    public List<TodoItemDto> findAllIncompleteOrDateTodoBySubGoalId(UUID subGoalId,
            LocalDate today) {
        QTodoEntity todoEntity = QTodoEntity.todoEntity;
        QTodoResultEntity todoResultEntity = QTodoResultEntity.todoResultEntity;

        return todoItemJPAQuery(todoEntity, todoResultEntity)
                .where(
                        todoEntity.subGoalId.eq(subGoalId)
                                .and(anyOf(
                                        todoEntity.status.ne(TodoStatus.COMPLETE),
                                        todoEntity.date.eq(today)
                                ))
                )
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
    public void deleteAllTodoCascadeBySubGoalId(UUID subGoalId) {
        QTodoResultEntity todoResult = QTodoResultEntity.todoResultEntity;
        QTodoEntity todo = QTodoEntity.todoEntity;

        jpaQueryFactory
                .delete(todoResult)
                .where(todoResult.todoId.in(
                        JPAExpressions
                                .select(todo.id)
                                .from(todo)
                                .where(todo.subGoalId.eq(subGoalId))
                ))
                .execute();

        todoJpaRepository.deleteAllBySubGoalId(subGoalId);
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

    @Override
    public List<Todo> findAllByIdsIn(Set<UUID> todoIds) {
        return todoJpaRepository.findAllByIdIn(todoIds)
                .stream()
                .map(TodoMapper::toDomain)
                .toList();
    }

    @Override
    public CustomSlice<TodoItemDto> findAllBySubGoalIdWithSlice(UUID subGoalId, int offset,
            int size) {
        QTodoEntity todoEntity = QTodoEntity.todoEntity;
        QTodoResultEntity todoResultEntity = QTodoResultEntity.todoResultEntity;

        List<TodoItemDto> results = todoItemJPAQuery(todoEntity, todoResultEntity)
                .where(todoEntity.subGoalId.eq(subGoalId))
                .offset(offset)
                .limit(size + 1)
                .fetch();

        boolean hasNext = results.size() > size;
        if (hasNext) {
            results = results.subList(0, size);
        }

        return new CustomSlice<>(results, hasNext, offset, size);
    }

    private JPAQuery<TodoItemDto> todoItemJPAQuery(QTodoEntity todoEntity,
            QTodoResultEntity todoResultEntity) {

        ConstructorExpression<TodoResultItemDto> resultItem =
                Projections.constructor(TodoResultItemDto.class,
                        todoResultEntity.id,
                        todoResultEntity.emotion,
                        todoResultEntity.content,
                        todoResultEntity.filePath);

        ConstructorExpression<TodoItemDto> todoItem =
                Projections.constructor(TodoItemDto.class,
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
