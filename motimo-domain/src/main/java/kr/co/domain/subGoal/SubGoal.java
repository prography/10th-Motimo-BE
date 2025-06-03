package kr.co.domain.subGoal;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

public class SubGoal {
    public UUID id;
    public String title;
    public int importance;
    @Getter
    public boolean completed;
    public LocalDateTime completedChangedAt;

    @Builder(builderMethodName = "createSubGoal")
    private SubGoal(String title, int importance) {
        this.title = title;
        this.importance = importance;
        this.completed = false;
    }

    public void changeCompleted() {
        completed = !completed;
        completedChangedAt = LocalDateTime.now();
    }
}
