package kr.co.domain.group.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import kr.co.domain.group.Group;
import kr.co.domain.group.dto.GroupJoinDto;

public interface GroupRepository {
    Group create(Group group);
    Group join(GroupJoinDto dto);

    // 네이밍 고민
    Optional<Group> findAvailableGroupBySimilarDueDate(UUID userId, LocalDate dueDate);
}
