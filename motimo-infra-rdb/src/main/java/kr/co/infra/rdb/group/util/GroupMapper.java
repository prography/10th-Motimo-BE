package kr.co.infra.rdb.group.util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.co.domain.group.Group;
import kr.co.domain.group.GroupMember;
import kr.co.infra.rdb.group.entity.GroupEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GroupMapper {

    public static Group toDomain(GroupEntity entity) {
        return Group.builder()
                .id(entity.getId())
                .finishedDate(entity.getFinishedDate())
                .build();
    }

    public static Group toDomain(GroupEntity entity, List<GroupMember> members) {
        return Group.builder()
                .id(entity.getId())
                .finishedDate(entity.getFinishedDate())
                .members(members)
                .build();
    }

    public static Group toDomainWithName(UUID groupId, String name, LocalDateTime finishedDate) {
        return Group.builder()
                .id(groupId)
                .name(name)
                .finishedDate(finishedDate)
                .build();
    }

    public static GroupEntity toEntity(Group group) {
        return new GroupEntity(group.getId());
    }
}
