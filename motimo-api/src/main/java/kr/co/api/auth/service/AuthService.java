package kr.co.api.auth.service;

import kr.co.api.security.jwt.TokenProvider;
import kr.co.api.security.jwt.TokenResponse;
import kr.co.api.user.service.UserService;
import kr.co.domain.auth.exception.InvalidTokenException;
import kr.co.domain.auth.exception.TokenMismatchException;
import kr.co.domain.auth.oauth2.OAuth2UserInfo;
import kr.co.domain.user.model.ProviderType;
import kr.co.domain.user.model.Role;
import kr.co.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public void processOAuth2Login(OAuth2UserInfo oAuth2UserInfo, String registrationId) {
        if (userService.existsByEmail(oAuth2UserInfo.getEmail())) {
            updateExistingUserProfile(oAuth2UserInfo);
        } else {
            registerUser(oAuth2UserInfo, registrationId);
        }
    }

    public TokenResponse reissueToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException();
        }

        UUID userId = tokenProvider.getUserIdFromToken(refreshToken);
        UUID tokenId = tokenProvider.getTokenIdFromToken(refreshToken);

        String savedToken = refreshTokenService.getTokenByTokenId(tokenId);

        if (savedToken.isEmpty() || !savedToken.equals(refreshToken)) {
            throw new TokenMismatchException();
        }

        TokenResponse newToken = tokenProvider.createToken(userId);
        refreshTokenService.saveRefreshToken(userId, newToken.tokenId(), newToken.refreshToken());
        refreshTokenService.deleteByTokenId(tokenId);
        return newToken;
    }

    public void logout(UUID tokenId) {
        refreshTokenService.deleteByTokenId(tokenId);
    }

    private void updateExistingUserProfile(OAuth2UserInfo oAuth2UserInfo) {
        User existingUser = userService.findByEmail(oAuth2UserInfo.getEmail());
        userService.updateProfile(existingUser, oAuth2UserInfo.getName(), oAuth2UserInfo.getImageUrl());
    }

    private void registerUser(OAuth2UserInfo oAuth2UserInfo, String registrationId) {
        User newUser = User.builder()
                .nickname(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .profileImageUrl(oAuth2UserInfo.getImageUrl())
                .role(Role.USER)
                .providerType(ProviderType.valueOf(registrationId.toUpperCase()))
                .providerId(oAuth2UserInfo.getId())
                .build();

        userService.register(newUser);
    }
}
