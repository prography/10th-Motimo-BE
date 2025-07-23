package kr.co.infra.rdb.todo.util;

import kr.co.domain.todo.TodoResult;
import kr.co.infra.rdb.todo.entity.TodoResultEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TodoResultMapper {

    public static TodoResult toDomain(TodoResultEntity entity) {
        return TodoResult.builder()
                .id(entity.getId())
                .todoId(entity.getTodoId())
                .userId(entity.getUserId())
                .emotion(entity.getEmotion())
                .content(entity.getContent())
                .filePath(entity.getFilePath())
                .fileName(entity.getFileName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static TodoResultEntity toEntity(TodoResult domain) {
        return new TodoResultEntity(
                domain.getId(),
                domain.getTodoId(),
                domain.getUserId(),
                domain.getEmotion(),
                domain.getContent(),
                domain.getFilePath(),
                domain.getFileName()
        );
    }

}