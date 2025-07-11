package kr.co.domain.group.message.strategy;

import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.GroupMessageType;
import kr.co.domain.group.message.content.GroupLeaveContent;
import kr.co.domain.group.message.content.GroupMessageContent;
import org.springframework.stereotype.Component;

@Component
public class GroupLeaveMessageStrategy implements MessageContentStrategy {

    @Override
    public GroupMessageContent createContent(GroupMessage message, MessageContentData contentData) {
        return new GroupLeaveContent();
    }

    @Override
    public GroupMessageType getMessageType() {
        return GroupMessageType.LEAVE;
    }
}
