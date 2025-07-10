package kr.co.infra.rdb.group.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.UUID;
import kr.co.domain.group.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupMemberRepositoryImpl implements GroupMemberRepository {

    private final GroupMemberJpaRepository groupMemberJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void deleteByGroupIdAndMemberId(UUID groupId, UUID userId) {
        groupMemberJpaRepository.deleteByGroupIdAndUserId(groupId, userId);
    }

}
