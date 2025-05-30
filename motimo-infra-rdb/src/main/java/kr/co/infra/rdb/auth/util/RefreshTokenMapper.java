package kr.co.infra.rdb.auth.util;

import kr.co.domain.auth.model.RefreshToken;
import kr.co.infra.rdb.auth.entity.RefreshTokenEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RefreshTokenMapper {

    public static RefreshToken toDomain(RefreshTokenEntity entity) {
        if (entity == null) {
            return null;
        }
        return RefreshToken.builder()
                .id(entity.getId())
                .tokenId(entity.getTokenId())
                .userId(entity.getUserId())
                .token(entity.getToken())
                .build();
    }

    public static RefreshTokenEntity toEntity(RefreshToken refreshToken) {
        if (refreshToken == null) {
            return null;
        }
        return RefreshTokenEntity.builder()
                .id(refreshToken.getId())
                .tokenId(refreshToken.getTokenId())
                .userId(refreshToken.getUserId())
                .token(refreshToken.getToken())
                .build();
    }
}
