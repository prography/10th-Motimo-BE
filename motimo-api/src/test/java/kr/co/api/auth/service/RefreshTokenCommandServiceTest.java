package kr.co.api.auth.service;

import kr.co.domain.auth.repository.RefreshTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Refresh Token Service Command 테스트")
class RefreshTokenCommandServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenCommandService refreshTokenCommandService;

    @Test
    void 리프레시_토큰을_정상저장() {
        // given
        UUID userId = UUID.randomUUID();
        UUID tokenId = UUID.randomUUID();
        String refreshToken = "test-refresh-token";

        // when
        refreshTokenCommandService.saveRefreshToken(userId, tokenId, refreshToken);

        // then
        verify(refreshTokenRepository).save(
                argThat(token ->
                        token.getTokenId().equals(tokenId) &&
                                token.getUserId().equals(userId) &&
                                token.getToken().equals(refreshToken)
                ));
    }

    @Test
    void 토큰ID로_리프레시_토큰을_삭제() {
        // given
        UUID tokenId = UUID.randomUUID();

        // when
        refreshTokenCommandService.deleteByTokenId(tokenId);

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
                refreshTokenCommandService.saveRefreshToken(null, tokenId, refreshToken)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void tokenId를_null값으로_저장시_예외가_발생() {
        // given
        UUID userId = UUID.randomUUID();
        String refreshToken = "test-refresh-token";

        // when & then
        assertThatThrownBy(() ->
                refreshTokenCommandService.saveRefreshToken(userId, null, refreshToken)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void refreshToken를_null값으로_저장시_예외가_발생() {
        // given
        UUID userId = UUID.randomUUID();
        UUID tokenId = UUID.randomUUID();

        // when & then
        assertThatThrownBy(() ->
                refreshTokenCommandService.saveRefreshToken(userId, tokenId, null)
        ).isInstanceOf(NullPointerException.class);
    }

}