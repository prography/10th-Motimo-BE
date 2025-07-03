package kr.co.infra.rdb.group.util;

import kr.co.domain.group.Group;
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

    public static GroupEntity toEntity(Group goal) {
        return new GroupEntity();
    }
}
