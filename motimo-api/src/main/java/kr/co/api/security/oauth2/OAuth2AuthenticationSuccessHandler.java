package kr.co.api.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import kr.co.api.auth.service.RefreshTokenCommandService;
import kr.co.api.security.UserPrincipal;
import kr.co.api.security.jwt.TokenProvider;
import kr.co.api.security.jwt.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenCommandService refreshTokenCommandService;
    private final CustomOAuth2AuthorizationRequestRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        TokenResponse token = tokenProvider.createToken(userPrincipal.getId());
        refreshTokenCommandService.saveRefreshToken(userPrincipal.getId(), token.tokenId(),
                token.refreshToken());

        String redirectUri = authorizationRequestRepository.getAndRemoveRedirectUriParam(request);

        if (StringUtils.hasText(redirectUri)) {
            // 토큰을 request parameter로 추가
            String finalRedirectUri = buildRedirectUriWithTokens(redirectUri, token,
                    userPrincipal.getCreatedAt());
            getRedirectStrategy().sendRedirect(request, response, finalRedirectUri);
        } else {
            // 기본 리다이렉션
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    private String buildRedirectUriWithTokens(String redirectUri, TokenResponse token,
            LocalDateTime createdAt) {
        try {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(redirectUri)
                    .queryParam("access_token", token.accessToken())
                    .queryParam("refresh_token", token.refreshToken())
                    .queryParam("created_at", createdAt);
            return uriBuilder.build().toUriString();
        } catch (Exception e) {
            // 예외 발생 시 원본 URI 반환
            return redirectUri;
        }
    }
}
