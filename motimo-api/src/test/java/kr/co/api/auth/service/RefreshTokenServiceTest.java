package kr.co.api.auth.service;

import kr.co.domain.auth.exception.TokenNotFoundException;
import kr.co.domain.auth.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void 리프레시_토큰을_정상저장() {
        // given
        UUID userId = UUID.randomUUID();
        UUID tokenId = UUID.randomUUID();
        String refreshToken = "test-refresh-token";

        // when
        refreshTokenService.saveRefreshToken(userId, tokenId, refreshToken);

        // then
        verify(refreshTokenRepository).save(
                argThat(token ->
                        token.getTokenId().equals(tokenId) &&
                        token.getUserId().equals(userId) &&
                        token.getToken().equals(refreshToken)
        ));
    }

    @Test
    void 토큰ID로_리프레시_토큰을_조회() {
        // given
        UUID tokenId = UUID.randomUUID();
        String expectedToken = "test-refresh-token";

        when(refreshTokenRepository.getTokenByTokenId(tokenId)).thenReturn(expectedToken);

        // when
        String result = refreshTokenService.getTokenByTokenId(tokenId);

        // then
        assertThat(result).isEqualTo(expectedToken);
        verify(refreshTokenRepository).getTokenByTokenId(tokenId);
    }

    @Test
    void 존재하지_않는_토큰ID로_리프레시_토큰_조회시_예외() {
        // given
        UUID tokenId = UUID.randomUUID();

        when(refreshTokenRepository.getTokenByTokenId(tokenId))
                .thenThrow(new TokenNotFoundException());

        // when & then
        assertThatThrownBy(() -> refreshTokenService.getTokenByTokenId(tokenId))
                .isInstanceOf(TokenNotFoundException.class);

        verify(refreshTokenRepository).getTokenByTokenId(tokenId);
    }

    @Test
    void 토큰ID로_리프레시_토큰을_삭제() {
        // given
        UUID tokenId = UUID.randomUUID();

        // when
        refreshTokenService.deleteByTokenId(tokenId);

        // then
        verify(refreshTokenRepository).deleteByTokenId(tokenId);
    }

    @Test
    void userId를_null값으로_저장시_예외가_발생() {
        // given
        UUID tokenId = UUID.randomUUID();
        String refreshToken = "test-refresh-token";

        // when & then
        assertThatThrownBy(() ->
                refreshTokenService.saveRefreshToken(null, tokenId, refreshToken)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void tokenId를_null값으로_저장시_예외가_발생() {
        // given
        UUID userId = UUID.randomUUID();
        String refreshToken = "test-refresh-token";

        // when & then
        assertThatThrownBy(() ->
                refreshTokenService.saveRefreshToken(userId, null, refreshToken)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void refreshToken를_null값으로_저장시_예외가_발생() {
        // given
        UUID userId = UUID.randomUUID();
        UUID tokenId = UUID.randomUUID();

        // when & then
        assertThatThrownBy(() ->
                refreshTokenService.saveRefreshToken(userId, tokenId, null)
        ).isInstanceOf(NullPointerException.class);
    }
}