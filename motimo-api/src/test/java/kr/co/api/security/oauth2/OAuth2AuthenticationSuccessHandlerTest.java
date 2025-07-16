package kr.co.api.security.oauth2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
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
import org.springframework.security.web.RedirectStrategy;

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

    @Mock
    private RedirectStrategy redirectStrategy;

    @InjectMocks
    private OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    @Test
//    void redirectUri가_없을_때_기본_리다이렉트가_실행된다() throws Exception {
    void redirectUri가_있는_경우_토큰과_함께_리다이렉트한다() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID tokenId = UUID.randomUUID();
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";
        String redirectUri = "http://localhost:3000/auth/callback";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, tokenId);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getId()).thenReturn(userId);
        when(tokenProvider.createToken(userId)).thenReturn(tokenResponse);
        when(authorizationRequestRepository.getAndRemoveRedirectUriParam(request)).thenReturn(
                redirectUri);

        oauth2AuthenticationSuccessHandler.setRedirectStrategy(redirectStrategy);

        // when
        oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response,
                authentication);

        // then
        verify(tokenProvider).createToken(userId);
        verify(refreshTokenCommandService).saveRefreshToken(userId, tokenId, refreshToken);
        verify(authorizationRequestRepository).getAndRemoveRedirectUriParam(request);

        // 리다이렉트 URL에 토큰이 포함되어야 함
        String expectedRedirectUri =
                "http://localhost:3000/auth/callback?access_token=" + accessToken
                        + "&refresh_token=" + refreshToken;
        verify(redirectStrategy).sendRedirect(request, response, expectedRedirectUri);
    }

    @Test
    void redirectUri가_없는_경우_기본_리다이렉션을_수행한다() throws Exception {
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
        verify(tokenProvider).createToken(userId);
        verify(refreshTokenCommandService).saveRefreshToken(userId, tokenId, refreshToken);
        verify(authorizationRequestRepository).getAndRemoveRedirectUriParam(request);

        // 기본 리다이렉션이 수행되어야 함 (super.onAuthenticationSuccess 호출)
        // RedirectStrategy.sendRedirect는 호출되지 않아야 함
        verify(redirectStrategy, never()).sendRedirect(any(), any(), any());
    }

    @Test
    void redirectUri가_빈_문자열인_경우_기본_리다이렉션을_수행한다() throws Exception {
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
        when(authorizationRequestRepository.getAndRemoveRedirectUriParam(request)).thenReturn("");

        // when
        oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response,
                authentication);

        // then
        verify(tokenProvider).createToken(userId);
        verify(refreshTokenCommandService).saveRefreshToken(userId, tokenId, refreshToken);
        verify(authorizationRequestRepository).getAndRemoveRedirectUriParam(request);

        // 기본 리다이렉션이 수행되어야 함
        verify(redirectStrategy, never()).sendRedirect(any(), any(), any());
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
    void TokenProvider에서_예외_발생_시_예외가_전파된다() {
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
        verify(authorizationRequestRepository, never()).getAndRemoveRedirectUriParam(any());
    }

    @Test
    void RefreshTokenService에서_예외_발생_시_예외가_전파된다() {
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
        verify(authorizationRequestRepository, never()).getAndRemoveRedirectUriParam(any());
    }

    @Test
    void 정상_처리시_올바른_순서로_실행된다() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID tokenId = UUID.randomUUID();
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";
        String redirectUri = "http://localhost:3000/auth/callback";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, tokenId);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getId()).thenReturn(userId);
        when(tokenProvider.createToken(userId)).thenReturn(tokenResponse);
        when(authorizationRequestRepository.getAndRemoveRedirectUriParam(request)).thenReturn(
                redirectUri);

        oauth2AuthenticationSuccessHandler.setRedirectStrategy(redirectStrategy);

        // when
        oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response,
                authentication);

        // then
        InOrder inOrder = inOrder(tokenProvider, refreshTokenCommandService,
                authorizationRequestRepository, redirectStrategy);
        inOrder.verify(tokenProvider).createToken(userId);
        inOrder.verify(refreshTokenCommandService).saveRefreshToken(userId, tokenId, refreshToken);
        inOrder.verify(authorizationRequestRepository).getAndRemoveRedirectUriParam(request);
        inOrder.verify(redirectStrategy).sendRedirect(eq(request), eq(response), anyString());
    }

    @Test
    void 기존_쿼리_파라미터가_있는_redirectUri_처리() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID tokenId = UUID.randomUUID();
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";
        String redirectUri = "http://localhost:3000/auth/callback?existing=param";
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, tokenId);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getId()).thenReturn(userId);
        when(tokenProvider.createToken(userId)).thenReturn(tokenResponse);
        when(authorizationRequestRepository.getAndRemoveRedirectUriParam(request)).thenReturn(
                redirectUri);

        oauth2AuthenticationSuccessHandler.setRedirectStrategy(redirectStrategy);

        // when
        oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response,
                authentication);

        // then
        String expectedRedirectUri =
                "http://localhost:3000/auth/callback?existing=param&access_token=" + accessToken
                        + "&refresh_token=" + refreshToken;
        verify(redirectStrategy).sendRedirect(request, response, expectedRedirectUri);
    }
}