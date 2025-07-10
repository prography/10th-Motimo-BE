package kr.co.domain.group.repository;

import java.util.UUID;

public interface GroupMemberRepository {

    void deleteByGroupIdAndMemberId(UUID groupId, UUID userId);
}
