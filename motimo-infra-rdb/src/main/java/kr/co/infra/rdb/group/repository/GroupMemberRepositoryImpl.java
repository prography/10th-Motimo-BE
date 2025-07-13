package kr.co.infra.rdb.group.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import kr.co.domain.group.GroupMember;
import kr.co.domain.group.repository.GroupMemberRepository;
import kr.co.infra.rdb.group.repository.projection.GroupMemberUserProjection;
import kr.co.infra.rdb.group.util.GroupMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupMemberRepositoryImpl implements GroupMemberRepository {

    private final GroupMemberJpaRepository groupMemberJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GroupMember> findAllByGroupId(UUID groupId) {
        List<GroupMemberUserProjection> groupMemberEntities = groupMemberJpaRepository.findByGroupId(groupId);
        return groupMemberEntities.stream().map(GroupMemberMapper::toDomain).toList();
    }

    @Override
    public void deleteByGroupIdAndMemberId(UUID groupId, UUID userId) {
        groupMemberJpaRepository.deleteByGroupIdAndUserId(groupId, userId);
    }

}
