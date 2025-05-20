package kr.co.api.security.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {

    private static final String TEST_SECRET = "dGhpc2lzYXZlcnlsb25nc2VjcmV0a2V5Zm9ydGVzdGluZ3B1cnBvc2VzaW5qd3R0b2tlbnByb3ZpZGVy";
    private static final long TOKEN_EXPIRATION = 3600000; // 1 hour
    private static final String ISSUER = "test-issuer";

    @Mock
    private JwtProperties jwtProperties;
    private TokenProvider tokenProvider;

    @Test
    @DisplayName("정상 토큰 생성 및 검증")
    void createAndValidateToken_Success() {
        // given
        UUID userId = UUID.randomUUID();
        String email = "test@gmail.com";
        when(jwtProperties.getJwtSecret()).thenReturn(TEST_SECRET);
        when(jwtProperties.getTokenExpiration()).thenReturn(TOKEN_EXPIRATION);
        when(jwtProperties.getIssuer()).thenReturn(ISSUER);

        tokenProvider = new TokenProvider(jwtProperties);

        // when
        TokenResponse tokenResponse = tokenProvider.createToken(userId, email);
        String token = tokenResponse.accessToken();

        // then
        assertThat(tokenProvider.validateToken(token)).isTrue();
        assertThat(tokenProvider.getIdFromToken(token)).isEqualTo(userId);
    }

    @Test
    @DisplayName("잘못된 토큰 검증시 실패")
    void validateToken_InvalidToken_Fail() {
        // given
        String invalidToken = "invalid.jwt.token";
        when(jwtProperties.getJwtSecret()).thenReturn(TEST_SECRET);

        tokenProvider = new TokenProvider(jwtProperties);

        // when
        boolean result = tokenProvider.validateToken(invalidToken);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("만료된 토큰 검증시 실패")
    void validateToken_ExpiredToken_Fail() {
        // given
        UUID userId = UUID.randomUUID();
        String email = "expired@gmail.com";

        JwtProperties expiredJwtProperties = mock(JwtProperties.class);
        when(expiredJwtProperties.getJwtSecret()).thenReturn(TEST_SECRET);
        when(expiredJwtProperties.getTokenExpiration()).thenReturn(-1000L);
        when(expiredJwtProperties.getIssuer()).thenReturn(ISSUER);

        TokenProvider expiredProvider = new TokenProvider(expiredJwtProperties);
        String expiredToken = expiredProvider.createToken(userId, email).accessToken();

        when(jwtProperties.getJwtSecret()).thenReturn(TEST_SECRET);
        tokenProvider = new TokenProvider(jwtProperties);

        // when
        boolean result = tokenProvider.validateToken(expiredToken);

        // then
        assertThat(result).isFalse();
    }
}