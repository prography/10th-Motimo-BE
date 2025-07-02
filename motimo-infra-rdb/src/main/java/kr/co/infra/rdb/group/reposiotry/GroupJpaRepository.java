package kr.co.infra.rdb.group.reposiotry;

import java.util.UUID;
import kr.co.infra.rdb.group.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupJpaRepository extends JpaRepository<GroupEntity, UUID> {
}
