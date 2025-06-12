package kr.co.infra.rdb.subGoal.repository;

import java.util.UUID;
import kr.co.infra.rdb.subGoal.entity.SubGoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubGoalJpaRepository extends JpaRepository<SubGoalEntity, UUID> {

}
