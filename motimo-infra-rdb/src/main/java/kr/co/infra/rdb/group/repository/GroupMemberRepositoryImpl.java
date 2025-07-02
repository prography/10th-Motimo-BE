package kr.co.infra.rdb.group.repository;

import kr.co.domain.group.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupMemberRepositoryImpl implements GroupMemberRepository {
    private final GroupMemberJpaRepository groupMemberJpaRepository;
}
