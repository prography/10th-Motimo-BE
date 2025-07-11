package kr.co.domain.common.event.group.message;

import java.util.UUID;
import kr.co.domain.common.event.Event;
import lombok.Getter;

@Getter
public class TodoCompletedEvent extends Event {

    private UUID userId;
    private UUID subGoalId;
    private UUID todoId;
    private String todoTitle;

    public TodoCompletedEvent(UUID userId, UUID subGoalId, UUID todoId, String todoTitle) {
        this.userId = userId;
        this.subGoalId = subGoalId;
        this.todoId = todoId;
        this.todoTitle = todoTitle;
    }
}
