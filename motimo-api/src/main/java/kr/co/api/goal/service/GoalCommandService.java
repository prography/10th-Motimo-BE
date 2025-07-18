package kr.co.api.goal.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import kr.co.api.goal.dto.GoalCreateDto;
import kr.co.api.goal.dto.GoalUpdateDto;
import kr.co.api.goal.dto.SubGoalUpdateDto;
import kr.co.api.todo.service.TodoCommandService;
import kr.co.domain.goal.DueDate;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.domain.subGoal.SubGoal;
import kr.co.domain.subGoal.repository.SubGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalCommandService {

    private final GoalRepository goalRepository;
    private final SubGoalRepository subGoalRepository;
    private final TodoCommandService todoCommandService;

    public UUID createGoal(UUID userId, GoalCreateDto dto) {
        DueDate dueDate =
                dto.isPeriodByMonth() ? DueDate.of(dto.month()) : DueDate.of(dto.dueDate());

        AtomicInteger index = new AtomicInteger(1);

        List<SubGoal> subGoals = dto.subGoals().stream()
                .map(sub -> {
                    int currentIndex = index.getAndIncrement();
                    return SubGoal.createSubGoal()
                            .title(sub.title())
                            .order(currentIndex)
                            .build();
                }).toList();

        Goal createdGoal = goalRepository.create(Goal.createGoal()
                .userId(userId)
                .title(dto.title())
                .dueDate(dueDate)
                .subGoals(subGoals)
                .build());

        return createdGoal.getId();
    }

    public UUID updateGoal(UUID userId, UUID goalId, GoalUpdateDto dto) {
        Goal goal = goalRepository.findById(goalId);
        goal.validateOwner(userId);

        goal.update(dto.title(), getDueDate(dto));
        goal.putSubGoals(makeUpsertSubGoals(goalId, goal.getSubGoals(), dto.subGoals()));

        return goalRepository.update(goal).getId();
    }

    private DueDate getDueDate(GoalUpdateDto dto) {
        return dto.isPeriodByMonth() ? DueDate.of(dto.month()) : DueDate.of(dto.dueDate());
    }

    private List<SubGoal> makeUpsertSubGoals(UUID goalId, List<SubGoal> subGoals,
            List<SubGoalUpdateDto> newSubGoals) {
        List<UUID> subGoalIds = subGoals.stream().map(SubGoal::getId).toList();
        return newSubGoals.stream().map(
                subGoal -> {
                    if (subGoalIds.contains(subGoal.updateId())) {
                        SubGoal orgSubGoal = subGoals.stream()
                                .filter(s -> s.getId().equals(subGoal.updateId())).findFirst()
                                .get();

                        orgSubGoal.updateTitle(subGoal.title());
                        orgSubGoal.updateOrder(subGoal.order());
                        return orgSubGoal;
                    }
                    return SubGoal.createSubGoal().goalId(goalId).title(subGoal.title())
                            .order(subGoal.order()).build();
                }
        ).toList();
    }

    public UUID completeGoal(UUID userId, UUID goalId) {
        Goal goal = goalRepository.findById(goalId);

        goal.validateOwner(userId);

        goal.complete();

        return goalRepository.update(goal).getId();
    }

    public void deleteGoalCascade(UUID userId, UUID goalId) {
        Goal goal = goalRepository.findById(goalId);
        goal.validateOwner(userId);

        Set<UUID> subGoalIds = goal.getSubGoals().stream().map(SubGoal::getId).collect(Collectors.toSet());

        // 투두 삭제 (file 삭제 고려 안 한 버전)
        todoCommandService.deleteAllTodoAndResultBySubGoalIds(subGoalIds);

        // 목표 삭제
        subGoalRepository.deleteAllByGoalId(goalId);
        goalRepository.deleteById(goalId);
    }
}
