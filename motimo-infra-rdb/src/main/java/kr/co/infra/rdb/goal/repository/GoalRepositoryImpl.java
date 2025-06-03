package kr.co.infra.rdb.goal.repository;

import kr.co.domain.goal.Goal;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import kr.co.infra.rdb.goal.util.GoalMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GoalRepositoryImpl implements GoalRepository {
    private final GoalJpaRepository goalJpaRepository;

    public GoalRepositoryImpl(GoalJpaRepository goalJpaRepository) {
        this.goalJpaRepository = goalJpaRepository;
    }

    @Override
    public Goal save(Goal goal) {
        GoalEntity goalEntity = goalJpaRepository.save(GoalMapper.toEntity(goal));
        return null;
//        return GoalMapper.toDomain(goalEntity);
    }
}
