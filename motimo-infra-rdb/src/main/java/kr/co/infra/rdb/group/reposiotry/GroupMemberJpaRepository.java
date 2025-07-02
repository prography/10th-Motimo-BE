package kr.co.infra.rdb.group.reposiotry;

import java.util.List;
import java.util.UUID;
import kr.co.infra.rdb.group.entity.GroupMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberJpaRepository extends JpaRepository<GroupMemberEntity, UUID> {
    List<GroupMemberEntity> findAllByUserId(UUID userId);
}
