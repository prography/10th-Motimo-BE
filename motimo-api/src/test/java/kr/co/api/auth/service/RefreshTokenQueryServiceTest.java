package kr.co.api.auth.service;

import kr.co.domain.auth.exception.TokenNotFoundException;
import kr.co.domain.auth.repository.RefreshTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Refresh Token Service Query 테스트")
class RefreshTokenQueryServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenQueryService refreshTokenQueryService;

    @Test
    void 토큰ID로_리프레시_토큰을_조회() {
        // given
        UUID tokenId = UUID.randomUUID();
        String expectedToken = "test-refresh-token";

        when(refreshTokenRepository.getTokenByTokenId(tokenId)).thenReturn(expectedToken);

        // when
        String result = refreshTokenQueryService.getTokenByTokenId(tokenId);

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
        assertThatThrownBy(() -> refreshTokenQueryService.getTokenByTokenId(tokenId))
                .isInstanceOf(TokenNotFoundException.class);

        verify(refreshTokenRepository).getTokenByTokenId(tokenId);
    }

}