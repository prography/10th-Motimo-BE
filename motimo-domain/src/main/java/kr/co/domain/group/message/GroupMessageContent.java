package kr.co.domain.group.message;

public sealed interface GroupMessageContent
        permits TodoCompletedContent, TodoResultSubmittedContent, JoinContent {

    GroupMessageType getType();

    String getContent();
}
