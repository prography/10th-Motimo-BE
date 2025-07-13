package kr.co.api.auth.service;

import java.util.UUID;
import kr.co.api.security.jwt.TokenProvider;
import kr.co.api.security.jwt.TokenResponse;
import kr.co.domain.auth.exception.InvalidTokenException;
import kr.co.domain.auth.exception.TokenMismatchException;
import kr.co.domain.common.event.Events;
import kr.co.domain.common.event.UserLoginEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenCommandService refreshTokenCommandService;
    private final RefreshTokenQueryService refreshTokenQueryService;

    public TokenResponse reissueToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException();
        }

        UUID userId = tokenProvider.getUserIdFromToken(refreshToken);
        UUID tokenId = tokenProvider.getTokenIdFromToken(refreshToken);

        String savedToken = refreshTokenQueryService.getTokenByTokenId(tokenId);

        if (savedToken.isEmpty() || !savedToken.equals(refreshToken)) {
            throw new TokenMismatchException();
        }

        TokenResponse newToken = tokenProvider.createToken(userId);
        refreshTokenCommandService.saveRefreshToken(userId, newToken.tokenId(),
                newToken.refreshToken());
        refreshTokenCommandService.deleteByTokenId(tokenId);

        // 유저 최근 접속일 업데이트
        Events.publishEvent(new UserLoginEvent(userId));

        return newToken;
    }

    // TODO: access token => black list
    public void logout(UUID tokenId) {
        refreshTokenCommandService.deleteByTokenId(tokenId);
    }
}
