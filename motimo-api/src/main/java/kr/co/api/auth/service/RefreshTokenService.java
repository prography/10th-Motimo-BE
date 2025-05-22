package kr.co.api.auth.service;

import kr.co.domain.auth.model.RefreshToken;
import kr.co.domain.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public void saveRefreshToken(UUID userId, UUID tokenId, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .tokenId(tokenId)
                .userId(userId)
                .token(token)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    public String getTokenByTokenId(UUID tokenId) {
        return refreshTokenRepository.getTokenByTokenId(tokenId);
    }

    @Transactional
    public void deleteByTokenId(UUID tokenId) {
        refreshTokenRepository.deleteByTokenId(tokenId);
    }

}
