package kr.co.domain.group.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.co.domain.group.Group;
import kr.co.domain.group.dto.GroupJoinDto;

public interface GroupRepository {

    Group findById(UUID groupId);

    Group create(Group group);

    Group join(GroupJoinDto dto);

    Optional<Group> findByGoalId(UUID goalId);

    Optional<Group> findAvailableGroupBySimilarDueDate(UUID userId, LocalDate dueDate);

    List<Group> findAllGroupDetailByUserId(UUID userId);

    boolean existsByGoalId(UUID goalId);

}
