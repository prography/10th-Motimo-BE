package kr.co.domain.todo;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(builderMethodName = "createTodoResult")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TodoResult {

    private UUID id;
    private UUID todoId;
    private Emotion emotion;
    private String content;
    private String filePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
