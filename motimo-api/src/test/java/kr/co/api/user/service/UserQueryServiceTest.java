package kr.co.api.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import kr.co.api.user.dto.UserProfileDto;
import kr.co.domain.user.exception.UserNotFoundException;
import kr.co.domain.user.model.ProviderType;
import kr.co.domain.user.model.Role;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import kr.co.infra.storage.service.StorageService;
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

    @Mock
    private StorageService storageService;

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

    @Test
    void 이메일로_유저_조회_성공() {
        // given
        String email = "test@gmail.com";
        User expectedUser = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .nickname("테스트 사용자")
                .providerType(ProviderType.GOOGLE)
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(expectedUser);

        // when
        User foundUser = userQueryService.findByEmail(email);

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(email);
        assertThat(foundUser.getNickname()).isEqualTo("테스트 사용자");
        assertThat(foundUser.getProviderType()).isEqualTo(ProviderType.GOOGLE);
        assertThat(foundUser.getRole()).isEqualTo(Role.USER);

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void 존재하지_않는_이메일로_유저_조회시_예외_반환() {
        // given
        String nonExistentEmail = "notfound@gmail.com";
        when(userRepository.findByEmail(nonExistentEmail)).thenThrow(new UserNotFoundException());

        // when & then
        assertThatThrownBy(() -> userQueryService.findByEmail(nonExistentEmail))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 유저_프로필_조회_성공_프로필_이미지_있는_경우() {
        // given
        UUID userId = UUID.randomUUID();
        String profileImagePath = "profile/image.jpg";
        String expectedProfileUrl = "https://example.com/profile/image.jpg";

        User user = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .nickname("테스트 사용자")
                .providerType(ProviderType.GOOGLE)
                .role(Role.USER)
                .profileImagePath(profileImagePath)
                .build();

        when(userRepository.findById(userId)).thenReturn(user);
        when(storageService.getFileUrl(profileImagePath)).thenReturn(expectedProfileUrl);

        // when
        UserProfileDto profile = userQueryService.getProfile(userId);

        // then
        assertThat(profile).isNotNull();
        assertThat(profile.id()).isEqualTo(userId);
        assertThat(profile.email()).isEqualTo("test@gmail.com");
        assertThat(profile.username()).isEqualTo("테스트 사용자");
        assertThat(profile.profileUrl()).isEqualTo(expectedProfileUrl);

        verify(userRepository, times(1)).findById(userId);
        verify(storageService, times(1)).getFileUrl(profileImagePath);
    }

    @Test
    void 유저_프로필_조회_성공_프로필_이미지_없는_경우() {
        // given
        UUID userId = UUID.randomUUID();

        User user = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .nickname("테스트 사용자")
                .providerType(ProviderType.GOOGLE)
                .role(Role.USER)
                .profileImagePath(null)
                .build();

        when(userRepository.findById(userId)).thenReturn(user);

        // when
        UserProfileDto profile = userQueryService.getProfile(userId);

        // then
        assertThat(profile).isNotNull();
        assertThat(profile.id()).isEqualTo(userId);
        assertThat(profile.email()).isEqualTo("test@gmail.com");
        assertThat(profile.username()).isEqualTo("테스트 사용자");
        assertThat(profile.profileUrl()).isEqualTo("");

        verify(userRepository, times(1)).findById(userId);
        verify(storageService, never()).getFileUrl(any());
    }

    @Test
    void 유저_프로필_조회_성공_프로필_이미지_빈_문자열() {
        // given
        UUID userId = UUID.randomUUID();

        User user = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .nickname("테스트 사용자")
                .providerType(ProviderType.GOOGLE)
                .role(Role.USER)
                .profileImagePath("")
                .build();

        when(userRepository.findById(userId)).thenReturn(user);

        // when
        UserProfileDto profile = userQueryService.getProfile(userId);

        // then
        assertThat(profile).isNotNull();
        assertThat(profile.profileUrl()).isEqualTo("");

        verify(userRepository, times(1)).findById(userId);
        verify(storageService, never()).getFileUrl(any());
    }

    @Test
    void 존재하지_않는_유저_프로필_조회시_예외_반환() {
        // given
        UUID nonExistentUserId = UUID.randomUUID();
        when(userRepository.findById(nonExistentUserId)).thenThrow(new UserNotFoundException());

        // when & then
        assertThatThrownBy(() -> userQueryService.getProfile(nonExistentUserId))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, times(1)).findById(nonExistentUserId);
        verify(storageService, never()).getFileUrl(any());
    }

}