package kr.co.infra.rdb.group.message.repository;

import java.util.UUID;
import kr.co.infra.rdb.group.message.GroupMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMessageJpaRepository extends JpaRepository<GroupMessageEntity, UUID> {

    void deleteAllByMessageReferenceReferenceId(UUID referenceId);

}
