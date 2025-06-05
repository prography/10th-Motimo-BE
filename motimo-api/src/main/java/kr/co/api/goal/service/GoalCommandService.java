package kr.co.api.goal.service;

import java.util.List;
import java.util.UUID;
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

    public UUID createGoal(UUID userId, String goalTitle, DueDate dueDate, List<SubGoal> subGoals) {

        Goal createdGoal = goalRepository.save(Goal.createGoal()
                .userId(userId)
                .title(goalTitle)
                .dueDate(dueDate)
                .subGoals(subGoals)
                .build());

        return createdGoal.getId();
    }
}
