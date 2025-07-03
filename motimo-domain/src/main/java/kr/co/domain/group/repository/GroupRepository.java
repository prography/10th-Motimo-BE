package kr.co.domain.group.repository;

import java.util.UUID;
import kr.co.domain.group.Group;

public interface GroupRepository {

    Group findById(UUID groupId);
}
