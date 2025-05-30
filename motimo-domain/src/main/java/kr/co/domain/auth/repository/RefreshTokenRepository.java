package kr.co.domain.auth.repository;

import kr.co.domain.auth.model.RefreshToken;

import java.util.UUID;

public interface RefreshTokenRepository {

    void save(RefreshToken refreshToken);

    String getTokenByTokenId(UUID tokenId);

    void deleteByTokenId(UUID tokenId);
}
