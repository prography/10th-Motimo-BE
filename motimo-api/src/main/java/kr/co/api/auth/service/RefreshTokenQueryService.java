package kr.co.api.auth.service;

import kr.co.domain.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RefreshTokenQueryService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenQueryService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String getTokenByTokenId(UUID tokenId) {
        return refreshTokenRepository.getTokenByTokenId(tokenId);
    }
}
