package kr.co.api.group.service;

import java.util.Optional;
import java.util.UUID;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.domain.group.Group;
import kr.co.domain.group.dto.GroupJoinDto;
import kr.co.domain.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupCommandService {

    private final GoalRepository goalRepository;
    private final GroupRepository groupRepository;

    public UUID joinGroup(UUID userId, UUID goalId) {
        Goal goal = goalRepository.findByIdWithoutSubGoals(goalId);

        Optional<Group> matchedGroup = groupRepository.findAvailableGroupBySimilarDueDate(userId,
                goal.getDueDateValue());

        if (matchedGroup.isPresent()) {
            groupRepository.join(GroupJoinDto.builder()
                    .groupId(matchedGroup.get().getId())
                    .userId(userId)
                    .goalId(goalId).build());
            return matchedGroup.get().getId();
        }

        Group group = groupRepository.create(Group.createGoal().build());
        groupRepository.join(GroupJoinDto.builder()
                .groupId(group.getId())
                .userId(userId)
                .goalId(goalId).build());

        return group.getId();
    }
}
