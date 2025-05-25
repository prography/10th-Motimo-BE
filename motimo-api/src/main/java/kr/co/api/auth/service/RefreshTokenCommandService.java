package kr.co.api.auth.service;

import kr.co.domain.auth.model.RefreshToken;
import kr.co.domain.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class RefreshTokenCommandService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenCommandService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void saveRefreshToken(UUID userId, UUID tokenId, String refreshToken) {
        RefreshToken token = RefreshToken.builder()
                .tokenId(tokenId)
                .userId(userId)
                .token(refreshToken)
                .build();

        refreshTokenRepository.save(token);
    }

    public void deleteByTokenId(UUID tokenId) {
        refreshTokenRepository.deleteByTokenId(tokenId);
    }

}
