package kr.co.domain.subGoal.repository;

import java.util.UUID;
import kr.co.domain.subGoal.SubGoal;

public interface SubGoalRepository {
    SubGoal findBy(UUID subGoalId);
    void update(SubGoal subGoal);
}
