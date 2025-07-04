package kr.co.domain.group.message;

public record GroupJoinContent() implements GroupMessageContent {

    @Override
    public GroupMessageType getType() {
        return GroupMessageType.JOIN;
    }
}
