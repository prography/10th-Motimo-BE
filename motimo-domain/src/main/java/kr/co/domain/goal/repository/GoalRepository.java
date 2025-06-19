package kr.co.domain.goal.repository;

import java.util.UUID;
import kr.co.domain.goal.Goal;

public interface GoalRepository {
    Goal save(Goal goal);
    Goal findById(UUID id);
}

