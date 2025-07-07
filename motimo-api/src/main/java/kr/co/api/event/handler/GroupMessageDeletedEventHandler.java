package kr.co.api.event.handler;

import kr.co.api.group.service.GroupMessageCommandService;
import kr.co.domain.common.event.group.message.GroupMessageDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMessageDeletedEventHandler implements
        OutboxEventHandler<GroupMessageDeletedEvent> {

    private final GroupMessageCommandService groupMessageCommandService;

    @Override
    public Class<GroupMessageDeletedEvent> payloadType() {
        return GroupMessageDeletedEvent.class;
    }

    @Override
    public void handle(GroupMessageDeletedEvent event) {
        groupMessageCommandService.deleteAllByReferenceId(event.getReferenceId());
    }
}
