package kr.co.api.group.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.co.api.group.service.dto.JoinedGroupDto;
import kr.co.domain.group.Group;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.repository.GroupMessageRepository;
import kr.co.domain.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupQueryService {
    private final GroupRepository groupRepository;
    private final GroupMessageRepository groupMessageRepository;

    public List<JoinedGroupDto> getJoinedGroupList(UUID userId) {
        List<Group> groups = groupRepository.findAllGroupDetailByUserId(userId);

        return groups.stream().map(group -> {
            Optional<GroupMessage> groupLastMessage = groupMessageRepository.findLastGroupMessageByGroupId(group.getId());
            return new JoinedGroupDto(
                    group.getId(),
                    group.getName(),
                    groupLastMessage.map(GroupMessage::getSendAt).orElse(null),
                    true
            );
        }).toList();
    }
}
