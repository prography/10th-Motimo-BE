package kr.co.api.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.api.security.UserPrincipal;
import kr.co.api.security.jwt.TokenProvider;
import kr.co.api.security.jwt.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.io.PrintWriter;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2AuthenticationSuccessHandlerTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private UserPrincipal userPrincipal;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private OAuth2AuthenticationSuccessHandler handler;

    @Test
    @DisplayName("OAuth2 인증 성공 시 토큰이 JSON으로 반환된다")
    void onAuthenticationSuccess_returnsTokenAsJson() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        String email = "test@gmail.com";
        TokenResponse tokenResponse = new TokenResponse("accessToken");

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getId()).thenReturn(userId);
        when(userPrincipal.getEmail()).thenReturn(email);
        when(tokenProvider.createToken(userId, email)).thenReturn(tokenResponse);
        when(objectMapper.writeValueAsString(tokenResponse)).thenReturn("{\"accessToken\":\"accessToken\"}");
        when(response.getWriter()).thenReturn(printWriter);

        // when
        handler.onAuthenticationSuccess(request, response, authentication);

        // then
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("utf-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).write("{\"accessToken\":\"accessToken\"}");
    }
}