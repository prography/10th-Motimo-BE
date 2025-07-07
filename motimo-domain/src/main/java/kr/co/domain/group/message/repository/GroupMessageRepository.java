package kr.co.domain.group.message.repository;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.common.pagination.CursorResult;
import kr.co.domain.common.pagination.CustomCursor;
import kr.co.domain.common.pagination.PagingDirection;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.NewGroupMessages;

public interface GroupMessageRepository {

    GroupMessage create(GroupMessage groupMessage);

    CursorResult<GroupMessage> findAllByGroupIdWithCursor(
            UUID groupId, CustomCursor cursor, int limit, PagingDirection direction);

    NewGroupMessages findNewMessagesFromLatestDate(
            UUID groupId, LocalDateTime latestDate, UUID latestMessageId);

}
