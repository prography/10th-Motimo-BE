package kr.co.domain.group.message.content;

import kr.co.domain.group.message.GroupMessageType;

public record GroupJoinContent() implements GroupMessageContent {

    @Override
    public GroupMessageType getType() {
        return GroupMessageType.JOIN;
    }
}
