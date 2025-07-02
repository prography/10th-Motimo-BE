package kr.co.domain.group.message;

import java.util.UUID;
import kr.co.domain.todo.Emotion;

public record TodoResultSubmittedContent(
        UUID todoId,
        String todoTitle,
        UUID TodoResultId,
        Emotion emotion,
        String content,
        String fileUrl) implements GroupMessageContent {

    @Override
    public GroupMessageType getType() {
        return GroupMessageType.TODO_RESULT_SUBMIT;
    }
}
