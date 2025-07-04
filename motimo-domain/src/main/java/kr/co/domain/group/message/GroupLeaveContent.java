package kr.co.domain.group.message;

public record GroupLeaveContent() implements GroupMessageContent {

    @Override
    public GroupMessageType getType() {
        return GroupMessageType.LEAVE;
    }
}
