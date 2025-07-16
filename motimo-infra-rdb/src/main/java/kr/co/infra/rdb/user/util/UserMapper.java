package kr.co.infra.rdb.user.util;

import java.util.HashSet;
import kr.co.domain.user.model.User;
import kr.co.infra.rdb.user.entity.UserEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .bio(entity.getBio())
                .profileImagePath(entity.getProfileImagePath())
                .interests(new HashSet<>(entity.getInterests()))
                .role(entity.getRole())
                .providerType(entity.getProviderType())
                .providerId(entity.getProviderId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImagePath(user.getProfileImagePath())
                .bio(user.getBio())
                .interests(new HashSet<>(user.getInterests()))
                .role(user.getRole())
                .providerType(user.getProviderType())
                .providerId(user.getProviderId())
                .build();
    }
}