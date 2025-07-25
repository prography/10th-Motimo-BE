package kr.co.domain.subGoal.repository;

import java.util.List;
import java.util.UUID;
import kr.co.domain.subGoal.SubGoal;

public interface SubGoalRepository {
    SubGoal findById(UUID subGoalId);
    List<SubGoal> findAllByGoalId(UUID goalId);
    SubGoal create(SubGoal subGoal);
    SubGoal update(SubGoal subGoal);
    void updateAllOrder(List<SubGoal> subGoal);
    void deleteAllByGoalId(UUID goalId);
}
