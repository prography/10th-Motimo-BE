package kr.co.domain.group.message.strategy;

import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.GroupMessageType;
import kr.co.domain.group.message.content.GroupMessageContent;

public interface MessageContentStrategy {

    GroupMessageContent createContent(GroupMessage message, MessageContentData provider);

    GroupMessageType getMessageType();
}
