package kr.co.infra.rdb.user;

import kr.co.domain.user.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return new User(entity.getId(), entity.getUsername(), entity.getPassword());
    }

    public static UserEntity toEntity(User user) {
        if (user == null) return null;
        UserEntity entity = UserEntity.builder()
                .id(user.id())
                .username(user.username())
                .password(user.password())
                .build();
        return entity;
    }
}
