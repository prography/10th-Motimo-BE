package kr.co.infra.rdb.goal.repository;

import java.util.UUID;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.exception.GoalNotFoundException;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.infra.rdb.goal.entity.DueDateEmbeddable;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import kr.co.infra.rdb.goal.entity.GoalMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GoalRepositoryImpl implements GoalRepository {
    private final GoalJpaRepository goalJpaRepository;

    public GoalRepositoryImpl(GoalJpaRepository goalJpaRepository) {
        this.goalJpaRepository = goalJpaRepository;
    }

    public Goal create(Goal goal) {
        GoalEntity savedGoalEntity = goalJpaRepository.save(GoalMapper.toEntity(goal));

        return GoalMapper.toDomain(savedGoalEntity);
    }

    public Goal update(Goal goal) {
        GoalEntity goalEntity = goalJpaRepository.findById(goal.getId()).orElseThrow(
                GoalNotFoundException::new);
        goalEntity.update(goal.getTitle(), DueDateEmbeddable.from(goal.getDueDate()), goal.isCompleted(),goal.completedAt);
        GoalEntity savedGoal = goalJpaRepository.save(goalEntity);
        return GoalMapper.toDomain(savedGoal);
    }

    public Goal findById(UUID id) {
        GoalEntity goalEntity = goalJpaRepository.findById(id).orElseThrow(GoalNotFoundException::new);
        return GoalMapper.toDomain(goalEntity);
    }

}
