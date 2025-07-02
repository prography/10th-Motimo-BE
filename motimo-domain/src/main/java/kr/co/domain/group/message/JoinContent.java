package kr.co.domain.group.message;

public record JoinContent() implements GroupMessageContent {

    @Override
    public GroupMessageType getType() {
        return GroupMessageType.JOIN;
    }
}
