package kr.co.infra.rdb.subGoal.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import kr.co.domain.goal.exception.GoalNotFoundException;
import kr.co.domain.subGoal.SubGoal;
import kr.co.domain.subGoal.exception.SubGoalNotFoundException;
import kr.co.domain.subGoal.repository.SubGoalRepository;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import kr.co.infra.rdb.goal.repository.GoalJpaRepository;
import kr.co.infra.rdb.subGoal.entity.SubGoalEntity;
import kr.co.infra.rdb.subGoal.entity.SubGoalMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SubGoalRepositoryImpl implements SubGoalRepository {

    private final SubGoalJpaRepository subGoalJpaRepository;
    private final GoalJpaRepository goalJpaRepository;

    public SubGoalRepositoryImpl(SubGoalJpaRepository subGoalJpaRepository,
            GoalJpaRepository goalJpaRepository) {
        this.subGoalJpaRepository = subGoalJpaRepository;
        this.goalJpaRepository = goalJpaRepository;
    }

    public SubGoal findById(UUID subGoalId) {
        SubGoalEntity subGoalEntity = subGoalJpaRepository.findById(subGoalId)
                .orElseThrow(SubGoalNotFoundException::new);
        return SubGoalMapper.toDomain(subGoalEntity);
    }

    public List<SubGoal> findByGoalId(UUID goalId) {
        return subGoalJpaRepository.findByGoalId(goalId).stream().map(SubGoalMapper::toDomain).toList();
    }

    public void upsertList(UUID goalId, List<SubGoal> subGoals) {
        GoalEntity goalEntity = goalJpaRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);
        List<SubGoalEntity> subGoalEntities = subGoals.stream().map(subGoal -> SubGoalMapper.toEntity(goalEntity, subGoal)).toList();
        subGoalJpaRepository.saveAll(subGoalEntities);
    }

    public void deleteList(Set<UUID> subGoalIds) {
        goalJpaRepository.deleteAllById(subGoalIds);
    }

    public SubGoal create(SubGoal subGoal) {
        GoalEntity goalEntity = goalJpaRepository.findById(subGoal.getGoalId()).orElseThrow(GoalNotFoundException::new);
        SubGoalEntity subGoalEntity = SubGoalMapper.toEntity(goalEntity, subGoal);
        SubGoalEntity savedSubGoal = subGoalJpaRepository.save(subGoalEntity);
        return SubGoalMapper.toDomain(savedSubGoal);
    }

    public SubGoal update(SubGoal subGoal) {
        SubGoalEntity subGoalEntity = subGoalJpaRepository.findById(subGoal.getId()).orElseThrow(SubGoalNotFoundException::new);
        subGoalEntity.update(subGoal.getTitle(), subGoal.getImportance(), subGoal.isCompleted());
        SubGoalEntity savedSubGoal = subGoalJpaRepository.save(subGoalEntity);
        return SubGoalMapper.toDomain(savedSubGoal);
    }

    public void updateAllOrder(List<SubGoal> subGoals) {
        List<SubGoalEntity> subGoalEntities = subGoalJpaRepository.findAllById(subGoals.stream().map(SubGoal::getId).toList());

        subGoalEntities.forEach(subGoalEntity -> {
            subGoals.stream()
                    .filter(subGoal -> subGoal.getId().equals(subGoalEntity.getId()))
                    .findFirst()
                    .ifPresent(matched -> subGoalEntity.updateOrder(matched.getImportance()));
        });

        subGoalJpaRepository.saveAll(subGoalEntities);
    }
}
