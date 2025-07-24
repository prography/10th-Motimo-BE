package kr.co.domain.common.event.group.message;

import java.util.UUID;
import kr.co.domain.common.event.Event;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoResultSubmittedEvent extends Event {

    private UUID userId;
    private UUID subGoalId;
    private UUID todoId;
    private String todoTitle;
    private UUID todoResultId;
    private Emotion emotion;
    private String content;
    private String filePath;
    private String fileName;
    private String fileMimeType;

    public static TodoResultSubmittedEvent of(UUID userId, Todo todo, TodoResult todoResult) {
        return new TodoResultSubmittedEvent(
                userId, todo.getSubGoalId(), todo.getId(), todo.getTitle(), todoResult.getId(),
                todoResult.getEmotion(), todoResult.getContent(),
                todoResult.getFile().getFilePath(),
                todoResult.getFile().getFileName(),
                todoResult.getFile().getMimeType()
        );
    }
}
