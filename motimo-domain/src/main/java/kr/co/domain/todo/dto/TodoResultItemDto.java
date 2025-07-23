package kr.co.domain.todo.dto;

import java.util.UUID;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.TodoResult;

public record TodoResultItemDto(
        UUID id,
        Emotion emotion,
        String content,
        String fileUrl,
        String fileName
) {

    public static TodoResultItemDto of(TodoResult result, String fileUrl, String fileName) {
        return new TodoResultItemDto(
                result.getId(), result.getEmotion(), result.getContent(), fileUrl, fileName);
    }

    public TodoResultItemDto withFileUrl(String fileUrl, String fileName) {
        return new TodoResultItemDto(id, emotion, content, fileUrl, fileName);
    }
}
