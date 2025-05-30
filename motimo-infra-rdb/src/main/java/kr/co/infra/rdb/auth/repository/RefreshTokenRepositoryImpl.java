package kr.co.infra.rdb.auth.repository;

import kr.co.domain.auth.exception.TokenNotFoundException;
import kr.co.domain.auth.model.RefreshToken;
import kr.co.domain.auth.repository.RefreshTokenRepository;
import kr.co.infra.rdb.auth.entity.RefreshTokenEntity;
import kr.co.infra.rdb.auth.util.RefreshTokenMapper;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    public RefreshTokenRepositoryImpl(RefreshTokenJpaRepository refreshTokenJpaRepository) {
        this.refreshTokenJpaRepository = refreshTokenJpaRepository;
    }

    @Override
    public void save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = RefreshTokenMapper.toEntity(refreshToken);
        refreshTokenJpaRepository.save(entity);
    }

    @Override
    public String getTokenByTokenId(UUID tokenId) {
        RefreshTokenEntity entity = refreshTokenJpaRepository.findByTokenId(tokenId)
                .orElseThrow(TokenNotFoundException::new);
        return entity.getToken();
    }

    @Override
    public void deleteByTokenId(UUID tokenId) {
        refreshTokenJpaRepository.deleteByTokenId(tokenId);
    }
}
