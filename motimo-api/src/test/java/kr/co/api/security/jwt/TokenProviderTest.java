package kr.co.api.security.jwt;

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
    void 토큰_정상생성() {
        // given
        UUID userId = UUID.randomUUID();
        when(jwtProperties.getJwtSecret()).thenReturn(TEST_SECRET);
        when(jwtProperties.getTokenExpiration()).thenReturn(TOKEN_EXPIRATION);
        when(jwtProperties.getIssuer()).thenReturn(ISSUER);

        tokenProvider = new TokenProvider(jwtProperties);

        // when
        TokenResponse tokenResponse = tokenProvider.createToken(userId);
        String accessToken = tokenResponse.accessToken();

        // then
        assertThat(tokenProvider.validateToken(accessToken)).isTrue();
        assertThat(tokenProvider.getUserIdFromToken(accessToken)).isEqualTo(userId);
        assertThat(tokenProvider.getTokenIdFromToken(accessToken)).isNotNull();
    }

    @Test
    void 잘못된_토큰_검증시_실패() {
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
    void 만료된_토큰_검증시_실패() {
        // given
        UUID userId = UUID.randomUUID();
        JwtProperties expiredJwtProperties = mock(JwtProperties.class);
        when(expiredJwtProperties.getJwtSecret()).thenReturn(TEST_SECRET);
        when(expiredJwtProperties.getTokenExpiration()).thenReturn(-1000L);
        when(expiredJwtProperties.getIssuer()).thenReturn(ISSUER);

        TokenProvider expiredProvider = new TokenProvider(expiredJwtProperties);
        String expiredToken = expiredProvider.createToken(userId).accessToken();

        when(jwtProperties.getJwtSecret()).thenReturn(TEST_SECRET);
        tokenProvider = new TokenProvider(jwtProperties);

        // when
        boolean result = tokenProvider.validateToken(expiredToken);

        // then
        assertThat(result).isFalse();
    }
}