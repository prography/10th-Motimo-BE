package kr.co.domain.goal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.co.domain.subGoal.SubGoal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Goal {
    private UUID id;
    public boolean completed;
    private String title;
    private DueDate dueDate;
    private UUID userId;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    public LocalDateTime completedAt;
    private List<SubGoal> subGoals;

    public void addSubGoal(SubGoal subGoal) {
        subGoals.add(subGoal);
    }

    public void update(String title, DueDate dueDate, List<SubGoal> subGoals) {
        this.title = title;
        this.dueDate = dueDate;
        this.subGoals = subGoals;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        if (subGoals.stream().allMatch(SubGoal::isCompleted)) {
            completed = true;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Builder(builderMethodName = "createGoal")
    private Goal(UUID userId, String title, DueDate dueDate, List<SubGoal> subGoals) {
        this.userId = userId;
        this.title = title;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.completed = false;
        this.subGoals = subGoals;
    }
}
