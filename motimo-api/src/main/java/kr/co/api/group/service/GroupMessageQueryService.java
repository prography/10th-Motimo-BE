package kr.co.api.group.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import kr.co.api.group.service.message.MessageContentData;
import kr.co.api.group.service.message.MessageContentLoader;
import kr.co.api.group.service.message.MessageContentStrategyFactory;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.group.Group;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.content.GroupMessageContent;
import kr.co.domain.group.message.repository.GroupMessageRepository;
import kr.co.domain.group.message.strategy.MessageContentStrategy;
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
    private final GroupMessageRepository groupMessageRepository;
    private final MessageContentLoader contentLoader;
    private final MessageContentStrategyFactory strategyFactory;

    public CustomSlice<GroupMessageDto> findAllByGroupIdWithPaging(UUID userId, UUID groupId,
            int offset,
            int limit) {
        Group group = groupRepository.findById(groupId);
        // TODO: group에 참여하고 있는 유저인지 확인 로직 필요

        CustomSlice<GroupMessage> groupMessages = groupMessageRepository.findAllByGroupId(groupId,
                offset, limit);

        MessageContentData contentData = contentLoader.loadMessageContentData(
                groupMessages.content());

        List<GroupMessageDto> dtoList = groupMessages.content().stream()
                .map(message -> {
                    MessageContentStrategy strategy = strategyFactory.getStrategy(
                            message.getMessageType());
                    GroupMessageContent content = strategy.createContent(message, contentData);
                    if (content == null) {
                        return null;
                    }
                    boolean reacted = message.getReactions().stream()
                            .anyMatch(r -> r.getUserId().equals(userId));
                    String nickname = contentData.getUser(message.getUserId()).getNickname();
                    return GroupMessageDto.of(message, nickname, content, reacted);
                })
                .filter(Objects::nonNull)
                .toList();

        return new CustomSlice<>(dtoList, groupMessages.hasNext());
    }
}
