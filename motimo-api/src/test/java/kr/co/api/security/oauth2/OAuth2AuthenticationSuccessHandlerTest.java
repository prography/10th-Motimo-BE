package kr.co.api.security.oauth2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import kr.co.api.auth.service.RefreshTokenCommandService;
import kr.co.api.security.UserPrincipal;
import kr.co.api.security.jwt.TokenProvider;
import kr.co.api.security.jwt.TokenResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class OAuth2AuthenticationSuccessHandlerTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RefreshTokenCommandService refreshTokenCommandService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private UserPrincipal userPrincipal;

    @Mock
    private CustomOAuth2AuthorizationRequestRepository authorizationRequestRepository;

    @InjectMocks
    private OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    @Test
    void redirectUri가_없을_때_기본_리다이렉트가_실행된다() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID tokenId = UUID.randomUUID();
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, tokenId);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Mock 설정
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getId()).thenReturn(userId);
        when(tokenProvider.createToken(userId)).thenReturn(tokenResponse);
        when(authorizationRequestRepository.getAndRemoveRedirectUriParam(request)).thenReturn(null);

        // when
        oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response,
                authentication);

        // then
        verify(tokenProvider).createToken(userId);
        verify(refreshTokenCommandService).saveRefreshToken(userId, tokenId, refreshToken);
        verify(authorizationRequestRepository).getAndRemoveRedirectUriParam(request);
    }

    @Test
    void redirectUri가_있을_때_토큰과_함께_리다이렉트된다() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID tokenId = UUID.randomUUID();
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";
        String redirectUri = "http://localhost:3000/callback";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, tokenId);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getId()).thenReturn(userId);
        when(tokenProvider.createToken(userId)).thenReturn(tokenResponse);
        when(authorizationRequestRepository.getAndRemoveRedirectUriParam(request)).thenReturn(
                redirectUri);

        // when
        oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response,
                authentication);

        // then
        verify(tokenProvider).createToken(userId);
        verify(refreshTokenCommandService).saveRefreshToken(userId, tokenId, refreshToken);
        verify(authorizationRequestRepository).getAndRemoveRedirectUriParam(request);

        String redirectedUrl = response.getRedirectedUrl();
        assertThat(redirectedUrl).contains("access_token=" + accessToken);
        assertThat(redirectedUrl).contains("refresh_token=" + refreshToken);
        assertThat(redirectedUrl).startsWith(redirectUri);
    }

    @Test
    void 인증_주체가_UserPrincipal이_아닌_경우_예외가_발생한다() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(authentication.getPrincipal()).thenReturn("not-user-principal");

        // when & then
        assertThatThrownBy(() ->
                oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response,
                        authentication)
        ).isInstanceOf(ClassCastException.class);
    }

    @Test
    void TokenProvider에서_예외_발생_시() {
        // given
        UUID userId = UUID.randomUUID();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getId()).thenReturn(userId);
        when(tokenProvider.createToken(userId)).thenThrow(new RuntimeException("토큰 생성 실패"));

        // when & then
        assertThatThrownBy(() ->
                oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response,
                        authentication)
        ).isInstanceOf(RuntimeException.class)
                .hasMessage("토큰 생성 실패");

        verify(refreshTokenCommandService, never()).saveRefreshToken(any(), any(), any());
    }

    @Test
    void RefreshTokenService에서_예외_발생_시() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID tokenId = UUID.randomUUID();
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, tokenId);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getId()).thenReturn(userId);
        when(tokenProvider.createToken(userId)).thenReturn(tokenResponse);
        doThrow(new RuntimeException("refreshTokenService exception"))
                .when(refreshTokenCommandService).saveRefreshToken(userId, tokenId, refreshToken);

        // when & then
        assertThatThrownBy(() ->
                oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response,
                        authentication)
        ).isInstanceOf(RuntimeException.class)
                .hasMessage("refreshTokenService exception");

        verify(tokenProvider).createToken(userId);
        verify(refreshTokenCommandService).saveRefreshToken(userId, tokenId, refreshToken);
        verify(objectMapper, never()).writeValueAsString(any());
    }

    @Test
    void 정상으로_순서대로_실행된다() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID tokenId = UUID.randomUUID();
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, tokenId);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getId()).thenReturn(userId);
        when(tokenProvider.createToken(userId)).thenReturn(tokenResponse);
        when(authorizationRequestRepository.getAndRemoveRedirectUriParam(request)).thenReturn(null);

        // when
        oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response,
                authentication);

        // then
        InOrder inOrder = inOrder(tokenProvider, refreshTokenCommandService,
                authorizationRequestRepository);
        inOrder.verify(tokenProvider).createToken(userId);
        inOrder.verify(refreshTokenCommandService).saveRefreshToken(userId, tokenId, refreshToken);
        inOrder.verify(authorizationRequestRepository).getAndRemoveRedirectUriParam(request);
    }
}