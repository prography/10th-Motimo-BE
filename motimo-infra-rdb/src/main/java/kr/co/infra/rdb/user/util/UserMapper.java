package kr.co.infra.rdb.user.util;

import kr.co.infra.rdb.user.entity.UserEntity;
import kr.co.domain.user.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getPassword());
    }

    public static UserEntity toEntity(User user) {
        if (user == null) return null;
        return UserEntity.builder()
                .id(user.id())
                .nickname(user.nickname())
                .password(user.password())
                .build();
    }
}