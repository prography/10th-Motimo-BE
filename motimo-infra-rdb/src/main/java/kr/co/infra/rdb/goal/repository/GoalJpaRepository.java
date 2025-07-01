package kr.co.infra.rdb.goal.repository;

import java.util.List;
import java.util.UUID;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalJpaRepository extends JpaRepository<GoalEntity, UUID> {
    List<GoalEntity> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
    List<GoalEntity> findAllByGroupNullAndUserId(UUID userId);
}
