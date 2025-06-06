package kr.co.infra.rdb.goal.repository;

import java.util.UUID;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalJpaRepository extends JpaRepository<GoalEntity, UUID> {

}
