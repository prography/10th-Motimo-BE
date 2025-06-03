package kr.co.api.goal.service;

import java.util.List;
import java.util.UUID;
import kr.co.api.goal.rqrs.GoalCreateRq;
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

    public UUID createGoal(UUID userId, GoalCreateRq rq) {
        DueDate dueDate = DueDate.of(rq.month(), rq.dueDate());

        List<SubGoal> subGoals = rq.subGoals().stream()
                .map(subGoal -> SubGoal.createSubGoal().title(subGoal.title())
                        .importance(subGoal.importance()).build()).toList();

        Goal createdGoal = goalRepository.save(Goal.createGoal()
                .userId(userId)
                .title(rq.title())
                .dueDate(dueDate)
                .subGoals(subGoals)
                .build());

        return createdGoal.getId();
    }
}
