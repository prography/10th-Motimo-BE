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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 테스트")
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserService userService;

    @Mock
    private OAuth2UserInfo oAuth2UserInfo;

    @Mock
    private User existingUser;

    private UUID userId;
    private UUID tokenId;
    private String refreshToken;
    private String registrationId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        tokenId = UUID.randomUUID();
        refreshToken = "test-refresh-token";
        registrationId = "google";
    }

    @Nested
    @DisplayName("OAuth2 로그인 처리")
    class ProcessOAuth2LoginTest {

        @Test
        void 이미_존재하는_사용자인_경우_프로필_업데이트() {
            // given
            String email = "test@gmail.com";
            String name = "updatedName";
            String imageUrl = "updatedImageUrl";

            given(oAuth2UserInfo.getEmail()).willReturn(email);
            given(oAuth2UserInfo.getName()).willReturn(name);
            given(oAuth2UserInfo.getImageUrl()).willReturn(imageUrl);
            given(userService.existsByEmail(email)).willReturn(true);
            given(userService.findByEmail(email)).willReturn(existingUser);

            // when
            authService.processOAuth2Login(oAuth2UserInfo, registrationId);

            // then
            verify(userService).existsByEmail(email);
            verify(userService).findByEmail(email);
            verify(userService).updateProfile(existingUser, name, imageUrl);
            verify(userService, never()).register(any(User.class));
        }

        @Test
        void 존재하지_않은_이메일인_경우_새로운_사용자_회원가입() {
            // given
            String email = "newuser@gmail.com";
            String name = "username";
            String imageUrl = "imageUrl";
            String providerId = "googleId";

            given(oAuth2UserInfo.getEmail()).willReturn(email);
            given(oAuth2UserInfo.getName()).willReturn(name);
            given(oAuth2UserInfo.getImageUrl()).willReturn(imageUrl);
            given(oAuth2UserInfo.getId()).willReturn(providerId);
            given(userService.existsByEmail(email)).willReturn(false);

            // when
            authService.processOAuth2Login(oAuth2UserInfo, registrationId);

            // then
            verify(userService).existsByEmail(email);
            verify(userService, never()).findByEmail(anyString());
            verify(userService, never()).updateProfile(any(), anyString(), anyString());

            verify(userService).register(argThat(user ->
                    user.getNickname().equals(name) &&
                            user.getEmail().equals(email) &&
                            user.getProfileImageUrl().equals(imageUrl) &&
                            user.getRole() == Role.USER &&
                            user.getProviderType() == ProviderType.GOOGLE &&
                            user.getProviderId().equals(providerId)
            ));
        }
    }

    @Nested
    @DisplayName("토큰 재발급")
    class ReissueTokenTest {

        @Test
        void 유효한_리프레시_토큰으로_토큰_재발급() {
            // given
            String savedToken = refreshToken;
            TokenResponse newTokenResponse = new TokenResponse(
                    "new-access-token",
                    "new-refresh-token",
                    UUID.randomUUID()
            );

            given(tokenProvider.validateToken(refreshToken)).willReturn(true);
            given(tokenProvider.getUserIdFromToken(refreshToken)).willReturn(userId);
            given(tokenProvider.getTokenIdFromToken(refreshToken)).willReturn(tokenId);
            given(refreshTokenService.getTokenByTokenId(tokenId)).willReturn(savedToken);
            given(tokenProvider.createToken(userId)).willReturn(newTokenResponse);

            // when
            TokenResponse result = authService.reissueToken(refreshToken);

            // then
            assertThat(result).isEqualTo(newTokenResponse);

            verify(tokenProvider).validateToken(refreshToken);
            verify(tokenProvider).getUserIdFromToken(refreshToken);
            verify(tokenProvider).getTokenIdFromToken(refreshToken);
            verify(refreshTokenService).getTokenByTokenId(tokenId);
            verify(tokenProvider).createToken(userId);
            verify(refreshTokenService).saveRefreshToken(userId, newTokenResponse.tokenId(), newTokenResponse.refreshToken());
            verify(refreshTokenService).deleteByTokenId(tokenId);
        }

        @Test
        void 유효하지_않은_토큰으로_재발급_요청시_예외_발생() {
            // given
            given(tokenProvider.validateToken(refreshToken)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> authService.reissueToken(refreshToken))
                    .isInstanceOf(InvalidTokenException.class);

            verify(tokenProvider).validateToken(refreshToken);
            verify(tokenProvider, never()).getUserIdFromToken(anyString());
            verify(refreshTokenService, never()).getTokenByTokenId(any());
        }

        @Test
        void 저장된_리프레시_토큰이_없을_때_예외발생() {
            // given
            given(tokenProvider.validateToken(refreshToken)).willReturn(true);
            given(tokenProvider.getUserIdFromToken(refreshToken)).willReturn(userId);
            given(tokenProvider.getTokenIdFromToken(refreshToken)).willReturn(tokenId);
            given(refreshTokenService.getTokenByTokenId(tokenId)).willReturn("");

            // when & then
            assertThatThrownBy(() -> authService.reissueToken(refreshToken))
                    .isInstanceOf(TokenMismatchException.class);

            verify(tokenProvider).validateToken(refreshToken);
            verify(tokenProvider).getUserIdFromToken(refreshToken);
            verify(tokenProvider).getTokenIdFromToken(refreshToken);
            verify(refreshTokenService).getTokenByTokenId(tokenId);
            verify(tokenProvider, never()).createToken(any());
        }

        @Test
        void 토큰_불일치시_예외_발생() {
            // given
            String differentToken = "different-refresh-token";

            given(tokenProvider.validateToken(refreshToken)).willReturn(true);
            given(tokenProvider.getUserIdFromToken(refreshToken)).willReturn(userId);
            given(tokenProvider.getTokenIdFromToken(refreshToken)).willReturn(tokenId);
            given(refreshTokenService.getTokenByTokenId(tokenId)).willReturn(differentToken);

            // when & then
            assertThatThrownBy(() -> authService.reissueToken(refreshToken))
                    .isInstanceOf(TokenMismatchException.class);

            verify(tokenProvider).validateToken(refreshToken);
            verify(tokenProvider).getUserIdFromToken(refreshToken);
            verify(tokenProvider).getTokenIdFromToken(refreshToken);
            verify(refreshTokenService).getTokenByTokenId(tokenId);
            verify(tokenProvider, never()).createToken(any());
        }
    }

    @Nested
    @DisplayName("로그아웃 테스트")
    class LogoutTest {

        @Test
        void 로그아웃시_리프레시_토큰_삭제() {
            // given
            UUID tokenId = UUID.randomUUID();

            // when
            authService.logout(tokenId);

            // then
            verify(refreshTokenService).deleteByTokenId(tokenId);
        }
    }
}