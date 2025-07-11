package kr.co.domain.todo.dto;

import java.util.UUID;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.TodoResult;

public record TodoResultItem(
        UUID id,
        Emotion emotion,
        String content,
        String fileUrl
) {

    public static TodoResultItem of(TodoResult result, String fileUrl) {
        return new TodoResultItem(
                result.getId(), result.getEmotion(), result.getContent(), fileUrl);
    }

    public TodoResultItem withFileUrl(String fileUrl) {
        return new TodoResultItem(id, emotion, content, fileUrl);
    }
}
