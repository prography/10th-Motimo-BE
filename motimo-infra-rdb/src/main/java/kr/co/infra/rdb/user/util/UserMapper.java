package kr.co.infra.rdb.user.util;

import kr.co.infra.rdb.user.entity.UserEntity;
import kr.co.domain.user.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .profileImageUrl(entity.getProfileImageUrl())
                .role(entity.getRole())
                .providerType(entity.getProviderType())
                .providerId(entity.getProviderId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static UserEntity toEntity(User user) {
        if (user == null) return null;
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .role(user.getRole())
                .providerType(user.getProviderType())
                .providerId(user.getProviderId())
                .build();
    }
}