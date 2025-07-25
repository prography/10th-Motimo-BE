package kr.co.infra.rdb.goal.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalJpaRepository extends JpaRepository<GoalEntity, UUID> {

    List<GoalEntity> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

    List<GoalEntity> findAllByUserIdAndCompleted(UUID userId, boolean complete);

    Optional<GoalEntity> findBySubGoalsId(UUID subGoalId);

    List<GoalEntity> findAllByGroupIdNullAndUserId(UUID userId);

    List<GoalEntity> findAllByIdIn(Set<UUID> goalIds);
}
