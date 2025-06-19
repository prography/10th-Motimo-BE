package kr.co.api.goal.service;

import java.util.List;
import java.util.UUID;
import kr.co.api.goal.dto.GoalCreateDto;
import kr.co.domain.goal.DueDate;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.domain.subGoal.SubGoal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalCommandService {

    private final GoalRepository goalRepository;

    public UUID createGoal(UUID userId, GoalCreateDto dto) {
        DueDate dueDate =
                dto.isPeriodByMonth() ? DueDate.of(dto.month()) : DueDate.of(dto.dueDate());
        List<SubGoal> subGoals = dto.subGoals().stream().map(sub -> SubGoal.createSubGoal()
                .title(sub.title())
                .importance(sub.importance())
                .build()).toList();

        Goal createdGoal = goalRepository.create(Goal.createGoal()
                .userId(userId)
                .title(dto.title())
                .dueDate(dueDate)
                .subGoals(subGoals)
                .build());

        return createdGoal.getId();
    }

    public void goalComplete(UUID userId, UUID goalId) {
        Goal goal = goalRepository.findById(goalId);

        goal.validateOwner(userId);

        goal.complete();

        goalRepository.update(goal);
    }
}
