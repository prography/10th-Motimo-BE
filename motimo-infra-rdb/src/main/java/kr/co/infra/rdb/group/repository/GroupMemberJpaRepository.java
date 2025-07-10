package kr.co.infra.rdb.group.repository;

import java.util.Optional;
import java.util.UUID;
import kr.co.infra.rdb.group.entity.GroupMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberJpaRepository extends JpaRepository<GroupMemberEntity, UUID> {
    Optional<GroupMemberEntity> findByGoalId(UUID goalId);
}
