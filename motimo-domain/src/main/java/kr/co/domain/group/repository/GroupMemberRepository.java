package kr.co.domain.group.repository;

import java.util.List;
import java.util.UUID;
import kr.co.domain.group.GroupMember;

public interface GroupMemberRepository {

    List<GroupMember> findAllByGroupId(UUID groupId);
    void deleteByGroupIdAndMemberId(UUID groupId, UUID userId);
}
