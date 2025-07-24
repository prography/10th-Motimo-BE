package kr.co.domain.group.message.content;

import java.util.UUID;
import kr.co.domain.group.message.GroupMessageType;
import kr.co.domain.todo.Emotion;

public record TodoResultSubmittedContent(
        UUID todoId,
        String todoTitle,
        UUID todoResultId,
        Emotion emotion,
        String content,
        String fileUrl,
        String fileName,
        String mimeType) implements GroupMessageContent {

    @Override
    public GroupMessageType getType() {
        return GroupMessageType.TODO_RESULT_SUBMIT;
    }
}
