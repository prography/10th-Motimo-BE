package kr.co.infra.rdb.goal.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.exception.GoalNotFoundException;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.infra.rdb.goal.entity.DueDateEmbeddable;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import kr.co.infra.rdb.goal.entity.GoalMapper;
import kr.co.infra.rdb.subGoal.entity.SubGoalMapper;
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

        goalEntity.updateSubGoals(
                goal.getSubGoals().stream().map(s -> SubGoalMapper.toEntity(goalEntity, s))
                        .toList());

        goalEntity.update(goal.getTitle(), DueDateEmbeddable.from(goal.getDueDate()),
                goal.isCompleted(), goal.completedAt);

        GoalEntity savedGoal = goalJpaRepository.save(goalEntity);

        return GoalMapper.toDomain(savedGoal);
    }

    public Goal findById(UUID id) {
        GoalEntity goalEntity = goalJpaRepository.findById(id)
                .orElseThrow(GoalNotFoundException::new);
        return GoalMapper.toDomain(goalEntity);
    }

    public Goal findByIdWithoutSubGoals(UUID id) {
        GoalEntity goalEntity = goalJpaRepository.findById(id)
                .orElseThrow(GoalNotFoundException::new);
        return GoalMapper.toDomainWithoutSubGoal(goalEntity);
    }

    @Override
    public Goal findBySubGoalId(UUID subGoalId) {
        GoalEntity goalEntity = goalJpaRepository.findBySubGoalsId(subGoalId)
                .orElseThrow(GoalNotFoundException::new);
        return GoalMapper.toDomain(goalEntity);
    }

    public List<Goal> findAllByUserId(UUID userId) {
        List<GoalEntity> goalEntities = goalJpaRepository.findAllByUserIdOrderByCreatedAtDesc(
                userId);
        return goalEntities.stream().map(GoalMapper::toDomain).toList();
    }

    public List<Goal> findUnassignedGroupGoalsByUserId(UUID userId) {
        return goalJpaRepository.findAllByGroupIdNullAndUserId(userId).stream().map(
                GoalMapper::toDomainWithoutSubGoal
        ).toList();
    }

    @Override
    public List<Goal> findCompletedGoalsByUserId(UUID userId) {
        return goalJpaRepository.findAllByUserIdAndCompleted(userId, true)
                .stream().map(GoalMapper::toDomain).toList();
    }

    @Override
    public List<Goal> findAllByIdsIn(Set<UUID> goalIds) {
        return goalJpaRepository.findAllByIdIn(goalIds)
                .stream()
                .map(GoalMapper::toDomain)
                .toList();
    }

    @Override
    public void connectGroupByGoalId(UUID goalId, UUID groupId) {
        GoalEntity goalEntity = goalJpaRepository.findById(goalId).orElseThrow(
                GoalNotFoundException::new);

        goalEntity.connectGroup(groupId);

    }

    @Override
    public void disconnectGroupByGoalId(UUID goalId) {
        GoalEntity goalEntity = goalJpaRepository.findById(goalId).orElseThrow(
                GoalNotFoundException::new);

        goalEntity.disconnectGroup();
    }

    public void deleteById(UUID goalId) {
        goalJpaRepository.deleteById(goalId);
    }

}
