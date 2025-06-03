package kr.co.domain.goal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.co.domain.subGoal.SubGoal;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Goal{
    private UUID id;
    private String title;
    private DueDate dueDate;
    public boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
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
        if(subGoals.stream().allMatch(SubGoal::isCompleted)) {
            completed = true;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
