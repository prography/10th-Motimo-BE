package kr.co.domain.todo;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.common.exception.AccessDeniedException;
import kr.co.domain.todo.exception.TodoErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TodoResult {

    @Builder.Default
    private UUID id = null;
    private UUID todoId;
    private UUID userId;
    private Emotion emotion;
    private String content;
    private TodoResultFile file;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder(builderMethodName = "createTodoResult")
    private TodoResult(UUID todoId, UUID userId, Emotion emotion, String content,
            TodoResultFile file) {
        this.todoId = todoId;
        this.userId = userId;
        this.emotion = emotion;
        this.content = content;
        this.file = file;
    }

    public void validateOwner(UUID userId) {
        if (!this.userId.equals(userId)) {
            throw new AccessDeniedException(TodoErrorCode.TODO_RESULT_ACCESS_DENIED);
        }
    }

    public void update(Emotion emotion, String content, String filePath, String fileName,
            String mimeType) {
        this.emotion = emotion;
        this.content = content;
        this.file = TodoResultFile.of(filePath, fileName, mimeType);
    }
}
