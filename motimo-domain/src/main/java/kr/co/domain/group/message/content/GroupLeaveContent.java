package kr.co.domain.group.message.content;

import kr.co.domain.group.message.GroupMessageType;

public record GroupLeaveContent() implements GroupMessageContent {

    @Override
    public GroupMessageType getType() {
        return GroupMessageType.LEAVE;
    }
}
