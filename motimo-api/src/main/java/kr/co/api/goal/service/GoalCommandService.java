package kr.co.api.goal.service;

import java.util.List;
import java.util.UUID;
import kr.co.api.goal.dto.GoalCreateDto;
import kr.co.api.goal.dto.GoalUpdateDto;
import kr.co.api.goal.dto.SubGoalUpdateDto;
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

    public UUID createGoal(UUID userId, GoalCreateDto dto) {
        DueDate dueDate =
                dto.isPeriodByMonth() ? DueDate.of(dto.month()) : DueDate.of(dto.dueDate());
        List<SubGoal> subGoals = dto.subGoals().stream().map(sub -> SubGoal.createSubGoal()
                .title(sub.title())
                .order(0)
                .build()).toList();

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

        List<SubGoal> subGoals = subGoalRepository.findAllByGoalId(goalId);
        subGoalRepository.deleteList(dto.deletedSubGoalIds());
        subGoalRepository.upsertList(goalId, makeUpsertSubGoals(goalId, subGoals, dto.subGoals()));

        return goalRepository.update(goal).getId();
    }

    private DueDate getDueDate(GoalUpdateDto dto) {
        return dto.isPeriodByMonth() ? DueDate.of(dto.month()) : DueDate.of(dto.dueDate());
    }

    private List<SubGoal> makeUpsertSubGoals(UUID goalId, List<SubGoal> subGoals, List<SubGoalUpdateDto> newSubGoals) {
        List<UUID> subGoalIds = subGoals.stream().map(SubGoal::getId).toList();
        return newSubGoals.stream().map(
                subGoal -> {
                    if(subGoalIds.contains(subGoal.updateId())) {
                        SubGoal orgSubGoal =  subGoals.stream().filter(s -> s.getId().equals(subGoal.updateId())).findFirst().get();

                        orgSubGoal.updateTitle(subGoal.title());
                        orgSubGoal.updateOrder(subGoal.order());
                        return orgSubGoal;
                    }
                    return SubGoal.createSubGoal().goalId(goalId).title(subGoal.title()).order(subGoal.order()).build();
                }
        ).toList();
    }

    public UUID completeGoal(UUID userId, UUID goalId) {
        Goal goal = goalRepository.findById(goalId);

        goal.validateOwner(userId);

        goal.complete();

        return goalRepository.update(goal).getId();
    }
}
