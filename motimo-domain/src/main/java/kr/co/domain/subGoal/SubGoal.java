package kr.co.domain.subGoal;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

public class SubGoal {
    public UUID id;
    public String title;
    @Getter
    public boolean completed;
    public LocalDateTime completedChangedAt;

    public void changeCompleted() {
        completed = !completed;
        completedChangedAt = LocalDateTime.now();
    }
}
