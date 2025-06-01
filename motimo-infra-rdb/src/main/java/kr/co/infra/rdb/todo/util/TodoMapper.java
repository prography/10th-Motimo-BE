package kr.co.infra.rdb.todo.util;

import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import kr.co.infra.rdb.todo.entity.TodoEntity;
import kr.co.infra.rdb.todo.entity.TodoResultEmbeddable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TodoMapper {

    public static Todo toDomain(TodoEntity entity) {
        return Todo.builder()
                .id(entity.getId())
                .subGoalId(entity.getSubGoalId())
                .authorId(entity.getAuthorId())
                .title(entity.getTitle())
                .date(entity.getDate())
                .completed(entity.isCompleted())
                .result(toTodoResult(entity.getResult()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static TodoEntity toEntity(Todo domain) {
        return TodoEntity.builder()
                .id(domain.getId())
                .subGoalId(domain.getSubGoalId())
                .authorId(domain.getAuthorId())
                .title(domain.getTitle())
                .date(domain.getDate())
                .completed(domain.isCompleted())
                .result(toTodoResultEmbeddable(domain.getResult()))
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    private static TodoResult toTodoResult(TodoResultEmbeddable embeddable) {
        if (embeddable == null) {
            return null;
        }
        return new TodoResult(
                embeddable.getEmotion(),
                embeddable.getContent(),
                embeddable.getImageName()
        );
    }

    private static TodoResultEmbeddable toTodoResultEmbeddable(TodoResult result) {
        if (result == null) {
            return null;
        }
        return new TodoResultEmbeddable(
                result.getEmotion(),
                result.getContent(),
                result.getImageName()
        );
    }

}