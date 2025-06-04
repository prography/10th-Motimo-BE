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
                .emotion(entity.getEmotion())
                .content(entity.getContent())
                .fileUrl(entity.getFileUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static TodoResultEntity toEntity(TodoResult domain) {
        return TodoResultEntity.builder()
                .id(domain.getId())
                .todoId(domain.getTodoId())
                .emotion(domain.getEmotion())
                .content(domain.getContent())
                .fileUrl(domain.getFileUrl())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

}