package kr.co.api.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import kr.co.domain.user.exception.UserNotFoundException;
import kr.co.domain.user.model.ProviderType;
import kr.co.domain.user.model.Role;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Query 테스트")
class UserQueryServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserQueryService userQueryService;

    @Test
    void ID로_유저_조회_성공() {
        // given
        UUID userId = UUID.randomUUID();
        User expectedUser = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .nickname("테스트 사용자")
                .providerType(ProviderType.GOOGLE)
                .role(Role.USER)
                .build();

        when(userRepository.findById(userId)).thenReturn(expectedUser);

        // when
        User foundUser = userQueryService.findById(userId);

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(userId);
        assertThat(foundUser.getEmail()).isEqualTo("test@gmail.com");
        assertThat(foundUser.getNickname()).isEqualTo("테스트 사용자");
        assertThat(foundUser.getProviderType()).isEqualTo(ProviderType.GOOGLE);
        assertThat(foundUser.getRole()).isEqualTo(Role.USER);

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void 존재하지_않는_ID로_유저_조회시_예외_반환() {
        // given
        UUID nonExistentId = UUID.randomUUID();
        when(userRepository.findById(nonExistentId)).thenThrow(new UserNotFoundException());

        // when & then
        assertThatThrownBy(() -> userQueryService.findById(nonExistentId))
                .isInstanceOf(UserNotFoundException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"google", "kakao", "GOOGLE", "KAKAO"})
    void 이메일과_provider_타입으로_사용자_존재_확인(String provider) {
        // given
        String email = "test@gmail.com";
        ProviderType providerType = ProviderType.of(provider);

        when(userRepository.existsByEmailAndProviderType(email, providerType)).thenReturn(true);

        // when
        boolean exists = userQueryService.existsByEmailAndProviderType(email, providerType);

        // then
        assertThat(exists).isTrue();
        verify(userRepository, times(1)).existsByEmailAndProviderType(email, providerType);
    }

    @ParameterizedTest
    @ValueSource(strings = {"google", "kakao", "GOOGLE", "KAKAO"})
    void 이메일과_provider_타입으로_사용자_존재_확인시_존재하지_않음(String provider) {
        // given
        String email = "notfound@gmail.com";
        ProviderType providerType = ProviderType.of(provider);
        when(userRepository.existsByEmailAndProviderType(email, providerType)).thenReturn(false);

        // when
        boolean exists = userQueryService.existsByEmailAndProviderType(email, providerType);

        // then
        assertThat(exists).isFalse();
        verify(userRepository, times(1)).existsByEmailAndProviderType(email, providerType);
    }

    @ParameterizedTest
    @ValueSource(strings = {"google", "kakao", "GOOGLE", "KAKAO"})
    void 이메일과_provider_타입으로_사용자_조회(String provider) {
        // given
        String email = "test@gmail.com";
        ProviderType providerType = ProviderType.of(provider);
        User expectedUser = User.builder()
                .email(email)
                .providerType(providerType)
                .build();

        when(userRepository.findByEmailAndProviderType(email, providerType)).thenReturn(
                expectedUser);

        // when
        User actualUser = userQueryService.findByEmailAndProviderType(email, providerType);

        // then
        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getEmail()).isEqualTo(email);
        assertThat(actualUser.getProviderType()).isEqualTo(providerType);
        verify(userRepository, times(1)).findByEmailAndProviderType(email, providerType);
    }

    @ParameterizedTest
    @ValueSource(strings = {"google", "kakao", "GOOGLE", "KAKAO"})
    void 이메일과_provider_타입으로_사용자_조회시_사용자가_존재하지_않은_경우_예외_발생(String provider) {
        // given
        String email = "notfound@gmail.com";
        ProviderType providerType = ProviderType.of(provider);

        when(userRepository.findByEmailAndProviderType(email, providerType)).thenThrow(
                UserNotFoundException.class);

        // when & then
        assertThatThrownBy(() -> userQueryService.findByEmailAndProviderType(email, providerType))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, times(1)).findByEmailAndProviderType(email, providerType);
    }
}