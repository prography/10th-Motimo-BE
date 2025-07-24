package kr.co.domain.common.event.group.message;

import java.util.UUID;
import kr.co.domain.common.event.Event;
import lombok.Getter;

@Getter
public class GoalTitleUpdatedEvent extends Event {

    private UUID userId;
    private UUID goalId;
    private String title;
    private UUID groupId;

    public GoalTitleUpdatedEvent(UUID userId, UUID goalId, String title, UUID groupId) {
        this.userId = userId;
        this.goalId = goalId;
        this.title = title;
        this.groupId = groupId;
    }
}
