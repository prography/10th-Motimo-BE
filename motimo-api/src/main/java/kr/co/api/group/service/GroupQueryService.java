package kr.co.api.group.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.co.api.group.service.dto.GroupDto;
import kr.co.api.group.service.dto.GroupMemberDto;
import kr.co.api.group.service.dto.JoinedGroupDto;
import kr.co.domain.group.Group;
import kr.co.domain.group.GroupMember;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.repository.GroupMessageRepository;
import kr.co.domain.group.repository.GroupMemberRepository;
import kr.co.domain.group.repository.GroupRepository;
import kr.co.domain.notification.repository.NotificationRepository;
import kr.co.infra.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupQueryService {

    private final GroupRepository groupRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final NotificationRepository notificationRepository;
    private final StorageService storageService;

    public List<JoinedGroupDto> getJoinedGroupList(UUID userId) {
        List<Group> groups = groupRepository.findAllGroupDetailByUserId(userId);

        return groups.stream().map(group -> {
            Optional<GroupMessage> groupLastMessage = groupMessageRepository.findLastGroupMessageByGroupId(
                    group.getId());
            return new JoinedGroupDto(
                    group.getId(),
                    group.getName(),
                    groupLastMessage.map(GroupMessage::getSendAt).orElse(null),
                    true
            );
        }).toList();
    }

    public GroupDto getGroupDetail(UUID userId, UUID groupId) {
        Group group = groupRepository.findDetailByMemberIdAndGroupId(userId, groupId);
        return new GroupDto(group.getId(), group.getName());
    }

    public List<GroupMemberDto> getGroupMemberList(UUID userId, UUID groupId) {
        List<GroupMember> groupMembers = groupMemberRepository.findAllByGroupId(groupId);


        return groupMembers.stream()
                .map(member -> {
                    boolean isLoginUser = member.getMemberId().equals(userId);

                    Boolean isActivePoke = isLoginUser ? null
                            : !notificationRepository.existsByTodayPoke(userId, member.getMemberId(), groupId);

                    String profileUrl = resolveProfileImageUrl(member.getProfileImagePath());
                    return new GroupMemberDto(member.getMemberId(), member.getNickname(),
                            member.getLastOnlineDate(), isActivePoke);
                })
                .toList();
    }

    private String resolveProfileImageUrl(String path) {
        if (!StringUtils.hasText(path)) {
            return "";
        }
        return storageService.getFileUrl(path);
    }
}
