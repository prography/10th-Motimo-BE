package kr.co.domain.common.event.group.message;

import java.util.UUID;
import kr.co.domain.common.event.Event;
import lombok.Getter;

@Getter
public class GroupLeftEvent extends Event {

    private UUID groupId;
    private UUID userId;

    public GroupLeftEvent(UUID groupId, UUID userId) {
        this.groupId = groupId;
        this.userId = userId;
    }
}
