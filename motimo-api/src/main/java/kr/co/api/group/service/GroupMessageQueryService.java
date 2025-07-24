package kr.co.api.group.service;

import java.util.List;
import java.util.UUID;
import kr.co.api.group.service.dto.GroupChatDto;
import kr.co.api.group.service.dto.GroupMessageItemDto;
import kr.co.api.group.service.dto.NewMessageDto;
import kr.co.api.group.service.message.MessageContentLoader;
import kr.co.domain.common.pagination.CursorResult;
import kr.co.domain.common.pagination.CursorUtil;
import kr.co.domain.common.pagination.CustomCursor;
import kr.co.domain.common.pagination.PagingDirection;
import kr.co.domain.group.Group;
import kr.co.domain.group.exception.UserNotInGroupException;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.NewGroupMessages;
import kr.co.domain.group.message.content.GroupMessageContent;
import kr.co.domain.group.message.repository.GroupMessageRepository;
import kr.co.domain.group.message.strategy.MessageContentData;
import kr.co.domain.group.message.strategy.MessageContentStrategyFactory;
import kr.co.domain.group.repository.GroupMemberRepository;
import kr.co.domain.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupMessageQueryService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final MessageContentLoader contentLoader;
    private final MessageContentStrategyFactory strategyFactory;

    private final CursorUtil cursorUtil;

    public GroupChatDto findMessagesByGroupIdWithCursor(UUID userId, UUID groupId,
            String latestCursor, int limit, PagingDirection direction) {
        Group group = groupRepository.findById(groupId);
        if (!groupMemberRepository.existsByGroupIdAndMemberId(groupId, userId)) {
            throw new UserNotInGroupException();
        }

        // 커서값 파싱
        CustomCursor cursor = cursorUtil.parseCursor(latestCursor);

        // 메시지 조회
        CursorResult<GroupMessage> groupMessages = groupMessageRepository.findAllByGroupIdWithCursor(
                groupId, cursor, limit, direction);

        // DTO 변환
        MessageContentData contentData = contentLoader.loadMessageContentData(
                groupMessages.content());

        List<GroupMessageItemDto> messageItems = groupMessages.mapNotNull(msg -> {
            GroupMessageContent content =
                    strategyFactory.getStrategy(msg.getMessageType())
                            .createContent(msg, contentData);

            if (content == null) {
                return null;
            }

            boolean hasReacted = msg.getReactions().stream()
                    .anyMatch(r -> r.getUserId().equals(userId));

            String userName = contentData.getUser(msg.getUserId()).getNickname();
            return GroupMessageItemDto.of(msg, userName, content, hasReacted);
        }).content();

        // 커서 생성
        String nextCursor = null;
        String prevCursor = null;

        if (!messageItems.isEmpty()) {
            GroupMessageItemDto first = messageItems.getFirst(); // 가장 최신
            GroupMessageItemDto last = messageItems.getLast();  // 가장 오래된
            nextCursor = cursorUtil.createCursor(first.messageId(), first.sendAt());
            prevCursor = cursorUtil.createCursor(last.messageId(), last.sendAt());
        }

        return new GroupChatDto(
                messageItems,
                prevCursor,
                nextCursor,
                groupMessages.hasBefore(),
                groupMessages.hasAfter()
        );
    }

    public NewMessageDto findNewChats(UUID userId, UUID groupId, String latestCursor) {

        // 최신 커서 값이 없는 경우(처음 그룹에 들어온 경우)
        if (latestCursor == null) {
            return new NewMessageDto(false, 0, null, null);
        }

        Group group = groupRepository.findById(groupId);
//        if (!groupMemberRepository.existsByGroupIdAndMemberId(groupId, userId)) {
//            throw new UserNotInGroupException();
//        }

        CustomCursor cursor = cursorUtil.parseCursor(latestCursor);

        NewGroupMessages newMessages = groupMessageRepository.findNewMessagesFromLatestDate(
                groupId, cursor.dateTime(), cursor.id());

        String newLatestCursor = null;
        if (newMessages.latestMessageId() != null && newMessages.latestTime() != null) {
            newLatestCursor = cursorUtil.createCursor(newMessages.latestMessageId(),
                    newMessages.latestTime());
        }

        return new NewMessageDto(
                newMessages.count() > 0,
                newMessages.count(),
                newMessages.latestTime(),
                newLatestCursor
        );
    }
}
