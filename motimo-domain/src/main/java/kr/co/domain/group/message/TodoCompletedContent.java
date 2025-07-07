package kr.co.domain.group.message;

import java.util.UUID;

public record TodoCompletedContent(UUID todoId, String todoTitle) implements GroupMessageContent {

    @Override
    public GroupMessageType getType() {
        return GroupMessageType.TODO_COMPLETE;
    }

}