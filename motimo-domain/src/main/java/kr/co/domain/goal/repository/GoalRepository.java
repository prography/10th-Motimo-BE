package kr.co.domain.goal.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import kr.co.domain.goal.Goal;

public interface GoalRepository {

    Goal create(Goal goal);

    Goal update(Goal goal);

    Goal findById(UUID id);

    Goal findBySubGoalId(UUID subGoalId);

    Goal findByIdWithoutSubGoals(UUID id);

    List<Goal> findAllByUserId(UUID userId);

    List<Goal> findUnassignedGroupGoalsByUserId(UUID userId);

    List<Goal> findCompletedGoalsByUserId(UUID userId);

    List<Goal> findAllByIdsIn(Set<UUID> goalIds);

    void connectGroupByGoalId(UUID goalId, UUID groupId);

    void deleteById(UUID goalId);
}

