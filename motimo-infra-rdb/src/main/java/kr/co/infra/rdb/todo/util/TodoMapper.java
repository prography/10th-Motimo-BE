package kr.co.infra.rdb.todo.util;

import kr.co.domain.todo.Todo;
import kr.co.infra.rdb.todo.entity.TodoEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TodoMapper {

    public static Todo toDomain(TodoEntity entity) {
        return Todo.builder()
                .id(entity.getId())
                .subGoalId(entity.getSubGoalId())
                .title(entity.getTitle())
                .date(entity.getDate())
                .completed(entity.isCompleted())
                .result(entity.getResult())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static TodoEntity toEntity(Todo domain) {
        return TodoEntity.builder()
                .id(domain.getId())
                .subGoalId(domain.getSubGoalId())
                .title(domain.getTitle())
                .date(domain.getDate())
                .completed(domain.isCompleted())
                .result(domain.getResult())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

}