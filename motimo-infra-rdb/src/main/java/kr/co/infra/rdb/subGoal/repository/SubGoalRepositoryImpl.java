package kr.co.infra.rdb.subGoal.repository;

import java.util.UUID;
import kr.co.domain.subGoal.SubGoal;
import kr.co.domain.subGoal.exception.SubGoalNotFoundException;
import kr.co.domain.subGoal.repository.SubGoalRepository;
import kr.co.infra.rdb.subGoal.entity.SubGoalEntity;
import kr.co.infra.rdb.subGoal.entity.SubGoalMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SubGoalRepositoryImpl implements SubGoalRepository {
    private final SubGoalJpaRepository subGoalJpaRepository;

    public SubGoalRepositoryImpl(SubGoalJpaRepository subGoalJpaRepository) {
        this.subGoalJpaRepository = subGoalJpaRepository;
    }

    @Override
    public SubGoal findBy(UUID subGoalId) {
        SubGoalEntity subGoalEntity = subGoalJpaRepository.findById(subGoalId).orElseThrow(SubGoalNotFoundException::new);
        return SubGoalMapper.toDomain(subGoalEntity);
    }

    @Override
    public void update(SubGoal subGoal) {
        SubGoalEntity subGoalEntity = subGoalJpaRepository.findById(subGoal.getId()).orElseThrow(SubGoalNotFoundException::new);
        subGoalEntity.update(subGoal.getTitle(), subGoal.getImportance(), subGoal.isCompleted());
        subGoalJpaRepository.save(subGoalEntity);
    }
}
