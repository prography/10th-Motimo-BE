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
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .date(entity.getDate())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static TodoEntity toEntity(Todo domain) {
        return new TodoEntity(
                domain.getId(),
                domain.getSubGoalId(),
                domain.getUserId(),
                domain.getTitle(),
                domain.getDate(),
                domain.getStatus()
        );
    }

}