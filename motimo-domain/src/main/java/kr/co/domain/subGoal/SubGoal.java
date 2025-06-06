package kr.co.domain.subGoal;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SubGoal {
    private UUID id;
    private String title;
    private int importance;
    @Getter
    private boolean completed;
    private LocalDateTime completedChangedAt;

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
