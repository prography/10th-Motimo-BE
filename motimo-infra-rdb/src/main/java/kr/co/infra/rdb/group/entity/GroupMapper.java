package kr.co.infra.rdb.group.entity;

import kr.co.domain.group.Group;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GroupMapper {

    public static GroupEntity toEntity(Group group) {
        return new GroupEntity();
    }

    public static Group toDomain(GroupEntity entity) {
        return Group.builder()
                .build();
    }

}
