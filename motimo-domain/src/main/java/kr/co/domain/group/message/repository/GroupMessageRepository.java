package kr.co.domain.group.message.repository;

import java.util.UUID;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.group.message.GroupMessage;

public interface GroupMessageRepository {

    GroupMessage create(GroupMessage groupMessage);

    CustomSlice<GroupMessage> findAllByGroupId(UUID groupId, int offset, int limit);
}
