package kr.co.domain.group.message.content;

import java.util.UUID;
import kr.co.domain.group.message.GroupMessageType;

public record TodoCompletedContent(UUID todoId, String todoTitle) implements GroupMessageContent {

    @Override
    public GroupMessageType getType() {
        return GroupMessageType.TODO_COMPLETE;
    }

}