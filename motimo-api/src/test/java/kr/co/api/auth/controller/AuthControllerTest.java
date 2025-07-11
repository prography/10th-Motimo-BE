package kr.co.api.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import kr.co.api.auth.rqrs.TokenReissueRq;
import kr.co.api.auth.service.AuthService;
import kr.co.api.config.SecurityConfig;
import kr.co.api.config.WebConfig;
import kr.co.api.security.CustomUserDetailsService;
import kr.co.api.security.jwt.TokenProvider;
import kr.co.api.security.jwt.TokenResponse;
import kr.co.api.security.oauth2.CustomOAuth2AuthorizationRequestRepository;
import kr.co.api.security.oauth2.CustomOAuth2UserService;
import kr.co.api.security.oauth2.OAuth2AuthenticationFailureHandler;
import kr.co.api.security.oauth2.OAuth2AuthenticationSuccessHandler;
import kr.co.api.security.resolver.AuthTokenArgumentResolver;
import kr.co.api.security.resolver.AuthUserArgumentResolver;
import kr.co.domain.auth.exception.InvalidTokenException;
import kr.co.domain.auth.exception.TokenNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, WebConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private TokenProvider tokenProvider;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private CustomOAuth2UserService customOAuth2UserService;

    @MockitoBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @MockitoBean
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @MockitoBean
    private AuthUserArgumentResolver authUserArgumentResolver;

    @MockitoBean
    private AuthTokenArgumentResolver authTokenArgumentResolver;
    
    @MockitoBean
    private CustomOAuth2AuthorizationRequestRepository authorizationRequestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        reset(authTokenArgumentResolver);
    }

    @Test
    void 토큰_재발급_요청_성공() throws Exception {
        // given
        String refreshToken = "test-refresh-token";
        TokenReissueRq request = new TokenReissueRq(refreshToken);

        UUID newTokenId = UUID.randomUUID();
        TokenResponse response = new TokenResponse(
                "new-access-token",
                "new-refresh-token",
                newTokenId
        );

        when(authService.reissueToken(refreshToken)).thenReturn(response);

        // when & then
        mockMvc.perform(post("/v1/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    TokenResponse actualResponse = objectMapper.readValue(responseBody,
                            TokenResponse.class);

                    assertThat(actualResponse.accessToken()).isEqualTo(response.accessToken());
                    assertThat(actualResponse.refreshToken()).isEqualTo(response.refreshToken());
                });

        verify(authService).reissueToken(refreshToken);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-refresh-token", ""})
    void 잘못된_리프레시_토큰으로_재발급_요청시_예외_발생(String invalidRefreshToken) throws Exception {
        // given
        TokenReissueRq request = new TokenReissueRq(invalidRefreshToken);

        when(authService.reissueToken(invalidRefreshToken)).thenThrow(new InvalidTokenException());

        // when & then
        mockMvc.perform(post("/v1/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(authService).reissueToken(invalidRefreshToken);
    }

    @Test
    @WithMockUser
    void 로그아웃_요청_성공() throws Exception {
        // given
        UUID tokenId = UUID.randomUUID();

        when(authTokenArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(authTokenArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(tokenId);

        // when & then
        mockMvc.perform(post("/v1/auth/logout")
                        .header("Authorization", "Bearer test-token")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(authService).logout(tokenId);
    }

    @Test
    @WithMockUser
    void 로그아웃_처리_중_예외_발생시_오류가_반환() throws Exception {
        // given
        UUID tokenId = UUID.randomUUID();

        when(authTokenArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(authTokenArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(tokenId);

        doThrow(new TokenNotFoundException()).when(authService).logout(tokenId);

        // when & then
        mockMvc.perform(post("/v1/auth/logout")
                        .header("Authorization", "Bearer test-token")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(authService).logout(tokenId);
    }
}
