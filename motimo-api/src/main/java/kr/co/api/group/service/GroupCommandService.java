package kr.co.api.group.service;

import java.util.UUID;
import kr.co.domain.common.event.Events;
import kr.co.domain.common.event.group.message.GroupJoinedEvent;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.domain.group.Group;
import kr.co.domain.group.dto.GroupJoinDto;
import kr.co.domain.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupCommandService {

    private final GoalRepository goalRepository;
    private final GroupRepository groupRepository;

    @Transactional
    public UUID joinGroup(UUID userId, UUID goalId) {
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
        Events.publishEvent(new GroupJoinedEvent(groupId, userId));
        return groupRepository.join(joinDto).getId();
    }

    private UUID createAndJoinNewGroup(UUID userId, UUID goalId) {
        Group newGroup = groupRepository.create(Group.createGroup().build());
        joinUserToGroup(newGroup.getId(), userId, goalId);
        return newGroup.getId();
    }


}
