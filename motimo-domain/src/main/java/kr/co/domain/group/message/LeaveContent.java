package kr.co.domain.group.message;

public record LeaveContent() implements GroupMessageContent {

    @Override
    public GroupMessageType getType() {
        return GroupMessageType.LEAVE;
    }
}
