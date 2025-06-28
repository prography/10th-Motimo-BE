package kr.co.api.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.co.api.auth.service.RefreshTokenCommandService;
import kr.co.api.security.UserPrincipal;
import kr.co.api.security.jwt.TokenProvider;
import kr.co.api.security.jwt.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenCommandService refreshTokenCommandService;
    private final ObjectMapper objectMapper;
    private final OAuth2Properties oAuth2Properties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        TokenResponse token = tokenProvider.createToken(userPrincipal.getId());
        refreshTokenCommandService.saveRefreshToken(userPrincipal.getId(), token.tokenId(),
                token.refreshToken());

        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", token.accessToken())
                .httpOnly(true)          // JS로 못 읽음 → XSS 방어
                .path("/")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH_TOKEN", token.refreshToken())
                .httpOnly(true)
                .path("/")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        getRedirectStrategy().sendRedirect(request, response,
                oAuth2Properties.getFrontRedirectUrl());
    }
}
