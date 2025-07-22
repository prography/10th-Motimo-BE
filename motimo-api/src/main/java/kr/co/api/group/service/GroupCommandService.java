package kr.co.api.group.service;

import java.util.UUID;
import kr.co.domain.common.event.Events;
import kr.co.domain.common.event.NotificationSentEvent;
import kr.co.domain.common.event.group.message.GroupJoinedEvent;
import kr.co.domain.common.event.group.message.GroupLeftEvent;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.domain.group.Group;
import kr.co.domain.group.GroupMember;
import kr.co.domain.group.dto.GroupJoinDto;
import kr.co.domain.group.exception.AlreadyJoinedGroupException;
import kr.co.domain.group.exception.AlreadyTodayPokeException;
import kr.co.domain.group.repository.GroupMemberRepository;
import kr.co.domain.group.repository.GroupRepository;
import kr.co.domain.notification.NotificationType;
import kr.co.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupCommandService {

    private final GoalRepository goalRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final NotificationRepository notificationRepository;

    public UUID joinGroup(UUID userId, UUID goalId) {
        if (groupRepository.existsByGoalId(goalId)) {
            throw new AlreadyJoinedGroupException();
        }

        Goal goal = goalRepository.findByIdWithoutSubGoals(goalId);

        return groupRepository.findAvailableGroupBySimilarDueDate(userId, goal.getDueDateValue())
                .map(group -> joinUserToGroup(group.getId(), userId, goalId))
                .orElseGet(() -> createAndJoinNewGroup(userId, goalId));
    }

    private UUID joinUserToGroup(UUID groupId, UUID userId, UUID goalId) {
        GroupJoinDto joinDto = GroupJoinDto.builder()
                .groupId(groupId)
                .userId(userId)
                .goalId(goalId)
                .build();

        goalRepository.connectGroupByGoalId(goalId, groupId);

        Events.publishEvent(new GroupJoinedEvent(groupId, userId));
        return groupRepository.join(joinDto).getId();
    }

    private UUID createAndJoinNewGroup(UUID userId, UUID goalId) {
        Group newGroup = groupRepository.create(Group.createGroup().build());
        joinUserToGroup(newGroup.getId(), userId, goalId);
        return newGroup.getId();
    }

    public void leaveGroup(UUID userId, UUID groupId) {
        Group group = groupRepository.findById(groupId);
        GroupMember member = group.getMember(userId);

        groupRepository.left(groupId, member);
        goalRepository.disconnectGroupByGoalId(member.getGoalId());

        Events.publishEvent(new GroupLeftEvent(groupId, userId));
    }

    public void createPokeNotification(UUID userId, UUID groupId, UUID receiverId) {
        if (notificationRepository.existsByTodayPoke(userId, receiverId, groupId)) {
            throw new AlreadyTodayPokeException();
        }

        Events.publishEvent(new NotificationSentEvent(
                NotificationType.POKE,
                userId,
                receiverId,
                groupId,
                NotificationType.POKE.getDefaultTitle()
        ));
    }

}
