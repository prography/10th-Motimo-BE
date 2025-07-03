package kr.co.infra.rdb.group.reposiotry;

import java.util.UUID;
import kr.co.domain.group.Group;
import kr.co.domain.group.exception.GroupNotFoundException;
import kr.co.domain.group.repository.GroupRepository;
import kr.co.infra.rdb.group.entity.GroupEntity;
import kr.co.infra.rdb.group.entity.GroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {

    private final GroupJpaRepository groupJpaRepository;

    @Override
    public Group findById(UUID groupId) {
        GroupEntity groupEntity = groupJpaRepository.findById(groupId)
                .orElseThrow(GroupNotFoundException::new);
        return GroupMapper.toDomain(groupEntity);
    }
}
