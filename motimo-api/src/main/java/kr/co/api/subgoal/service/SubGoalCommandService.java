package kr.co.api.subgoal.service;

import java.util.UUID;
import kr.co.domain.subGoal.SubGoal;
import kr.co.domain.subGoal.repository.SubGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SubGoalCommandService {

    private final SubGoalRepository subGoalRepository;

    public void toggleSubGoalComplete(UUID userId, UUID subGoalId) {
        SubGoal subGoal = subGoalRepository.findBy(subGoalId);

        subGoal.userChecked(userId);

        subGoal.toggleCompleted();

        subGoalRepository.update(subGoal);
    }

}
