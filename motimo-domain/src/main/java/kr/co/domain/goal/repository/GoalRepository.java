package kr.co.domain.goal.repository;

import java.util.List;
import java.util.UUID;
import kr.co.domain.goal.Goal;

public interface GoalRepository {

    Goal create(Goal goal);

    Goal update(Goal goal);

    Goal findById(UUID id);

    Goal findByIdWithoutSubGoals(UUID id);

    List<Goal> findAllByUserId(UUID userId);

    List<Goal> findUnassignedGroupGoalsByUserId(UUID userId);

    List<Goal> findCompletedGoalsByUserId(UUID userId);
}

