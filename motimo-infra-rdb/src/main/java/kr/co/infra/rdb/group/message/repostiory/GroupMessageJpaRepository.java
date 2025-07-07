package kr.co.infra.rdb.group.message.repostiory;

import java.util.UUID;
import kr.co.infra.rdb.group.message.GroupMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMessageJpaRepository extends JpaRepository<GroupMessageEntity, UUID> {

}
