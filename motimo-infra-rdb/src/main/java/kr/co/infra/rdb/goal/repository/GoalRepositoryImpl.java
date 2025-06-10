package kr.co.infra.rdb.goal.repository;

import java.util.List;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import kr.co.infra.rdb.goal.entity.GoalMapper;
import kr.co.infra.rdb.subGoal.entity.SubGoalEntity;
import kr.co.infra.rdb.subGoal.entity.SubGoalMapper;
import kr.co.infra.rdb.subGoal.repository.SubGoalJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class GoalRepositoryImpl implements GoalRepository {
    private final GoalJpaRepository goalJpaRepository;
    private final SubGoalJpaRepository subGoalJpaRepository;

    public GoalRepositoryImpl(GoalJpaRepository goalJpaRepository, SubGoalJpaRepository subGoalJpaRepository) {
        this.goalJpaRepository = goalJpaRepository;
        this.subGoalJpaRepository = subGoalJpaRepository;
    }

    @Override
    public Goal save(Goal goal) {
        GoalEntity savedGoalEntity = goalJpaRepository.save(GoalMapper.toEntity(goal));

        List<SubGoalEntity> subGoalEntities = goal.getSubGoals().stream()
                .map(subGoal -> SubGoalMapper.toEntity(goal.getUserId(), savedGoalEntity, subGoal))
                .toList();
        subGoalJpaRepository.saveAll(subGoalEntities);

        return GoalMapper.toDomain(savedGoalEntity);
    }
}
