package kr.co.api.subgoal.service;

import java.util.UUID;
import kr.co.api.goal.dto.SubGoalCreateDto;
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
public class SubGoalCommandService {

    private final GoalRepository goalRepository;
    private final SubGoalRepository subGoalRepository;

    public UUID createSubGoal(UUID userId, UUID goalId, SubGoalCreateDto dto) {
        Goal goal = goalRepository.findById(goalId);

        goal.validateOwner(userId);

        SubGoal subGoal = subGoalRepository.create(
                SubGoal.createSubGoal()
                        .goalId(goalId)
                        .title(dto.title())
                        .importance(dto.importance())
                        .build()
        );

        return subGoal.getId();
    }

    public UUID updateSubGoal(UUID userId, UUID subGoalId, String title, int order) {
        SubGoal subGoal = subGoalRepository.findById(subGoalId);

        subGoal.validateOwner(userId);

        subGoal.updateTitle(title);

        return subGoalRepository.update(subGoal).getId();
    }

    public UUID toggleSubGoalComplete(UUID userId, UUID subGoalId) {
        SubGoal subGoal = subGoalRepository.findById(subGoalId);

        subGoal.validateOwner(userId);

        subGoal.toggleCompleted();

        return subGoalRepository.update(subGoal).getId();
    }

    public void updateSubGoalOrder(UUID userId, UUID goalId, UUID subGoalId, int order) {
        Goal goal = goalRepository.findById(goalId);

        goal.validateOwner(userId);

        goal.updateSubGoalOrderAndSort(subGoalId, order);

        subGoalRepository.updateAllOrder(goal.getSubGoals());
    }

}
