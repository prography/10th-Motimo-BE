package kr.co.domain.subGoal;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.common.exception.AccessDeniedException;
import kr.co.domain.subGoal.exception.SubGoalErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SubGoal {
    private UUID id;
    private UUID goalId;
    private UUID userId;
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

    public void toggleCompleted() {
        completed = !completed;
        completedChangedAt = LocalDateTime.now();
    }

    public void userChecked(UUID userId) {
        if(!userId.equals(this.userId)) {
            throw new AccessDeniedException(SubGoalErrorCode.SUB_GOAL_ACCESS_DENIED);
        }
    }
}
