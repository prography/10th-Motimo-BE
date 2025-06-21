package kr.co.api.goal.service;

import java.util.List;
import java.util.UUID;
import kr.co.api.goal.dto.GoalDetailDto;
import kr.co.api.goal.dto.GoalItemDto;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.domain.subGoal.SubGoal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoalQueryService {

    private final GoalRepository goalRepository;

    public GoalDetailDto getGoalDetail(UUID goalId) {
        Goal goal = goalRepository.findById(goalId);
        return new GoalDetailDto(
                goal.getId(),
                goal.getTitle(),
                goal.getDueDate().getDueDate(),
                calculateProgress(goal.getSubGoals()),
                goal.getCreatedAt()
        );
    }

    public List<GoalItemDto> getGoalList(UUID userId) {
        List<Goal> goals = goalRepository.findAllByUserId(userId);
        return goals.stream().map(goal -> new GoalItemDto(
                goal.getId(),
                goal.getTitle(),
                goal.getDueDate().getDueDate(),
                calculateProgress(goal.getSubGoals()),
                goal.getCreatedAt()
        )).toList();
    }

    private float calculateProgress(List<SubGoal> subGoals) {
        if (subGoals.isEmpty()) {
            return 0;
        }

        long completeCount = subGoals.stream().filter(SubGoal::isCompleted).count();

        return (float) completeCount / subGoals.size() * 100;
    }
}
