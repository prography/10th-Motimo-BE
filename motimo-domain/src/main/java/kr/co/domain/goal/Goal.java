package kr.co.domain.goal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kr.co.domain.common.exception.AccessDeniedException;
import kr.co.domain.goal.exception.GoalCompleteFailedException;
import kr.co.domain.goal.exception.GoalErrorCode;
import kr.co.domain.subGoal.SubGoal;
import kr.co.domain.subGoal.exception.SubGoalNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Goal {
    @Builder.Default()
    private UUID id = null;
    public boolean completed;
    private String title;
    private DueDate dueDate;
    private UUID userId;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    public LocalDateTime completedAt;
    @Builder.Default()
    private UUID groupId = null;
    @Builder.Default()
    private List<SubGoal> subGoals = new ArrayList<>();

    public void addSubGoals(List<SubGoal> subGoal) {
        subGoals.addAll(subGoal);
    }

    public void putSubGoals(List<SubGoal> subGoal) {
        this.subGoals = subGoal;
    }

    public void update(String title, DueDate dueDate) {
        this.title = title;
        this.dueDate = dueDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void joinedGroup(UUID groupId) {
        this.groupId = groupId;
    }

    public void complete() {
        if (subGoals.stream().allMatch(SubGoal::isCompleted)) {
            completed = true;
            completedAt = LocalDateTime.now();
        } else {
            throw new GoalCompleteFailedException();
        }
    }

    public void validateOwner(UUID userId) {
        if(!userId.equals(this.userId)) {
            throw new AccessDeniedException(GoalErrorCode.GOAL_ACCESS_DENIED);
        }
    }

    public float calculateProgress() {
        if(subGoals.isEmpty()) {
            return 0;
        }

        long completedSubGoalCount = subGoals.stream().filter(SubGoal::isCompleted).count();

        return (float) completedSubGoalCount / subGoals.size() * 100;
    }

    public LocalDate getDueDateValue() {
        return this.dueDate.getDate();
    }

    public void updateSubGoalOrderAndSort(UUID subGoalId, int newOrder) {
        SubGoal targetSubGoal = this.subGoals.stream()
                .filter(sg -> sg.getId().equals(subGoalId))
                .findFirst()
                .orElseThrow(SubGoalNotFoundException::new);

        int oldOrder = targetSubGoal.getOrder();

        if (oldOrder == newOrder) return;

        shiftOrdersBetween(oldOrder, newOrder);
        targetSubGoal.updateOrder(newOrder);
    }

    private void shiftOrdersBetween(int oldOrder, int newOrder) {
        for (SubGoal subGoal : subGoals) {
            int order = subGoal.getOrder();

            if (oldOrder < newOrder && order > oldOrder && order <= newOrder) {
                subGoal.updateOrder(order - 1); // 당김
            } else if (oldOrder > newOrder && order >= newOrder && order < oldOrder) {
                subGoal.updateOrder(order + 1); // 밀기
            }
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
