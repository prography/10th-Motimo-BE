package kr.co.domain.subGoal.repository;

import java.util.UUID;
import kr.co.domain.subGoal.SubGoal;

public interface SubGoalRepository {
    SubGoal findById(UUID subGoalId);
    SubGoal create(SubGoal subGoal);
    SubGoal update(SubGoal subGoal);
}
