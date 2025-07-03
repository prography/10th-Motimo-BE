package kr.co.infra.rdb.group.message.repostiory;

import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.repository.GroupMessageRepository;
import kr.co.infra.rdb.group.message.GroupMessageEntity;
import kr.co.infra.rdb.group.message.GroupMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupMessageRepositoryImpl implements GroupMessageRepository {

    private final GroupMessageJpaRepository groupMessageJpaRepository;

    @Override
    public GroupMessage create(GroupMessage groupMessage) {
        GroupMessageEntity entity = groupMessageJpaRepository.save(
                GroupMessageMapper.toEntity(groupMessage));
        return GroupMessageMapper.toDomain(entity);
    }
}
