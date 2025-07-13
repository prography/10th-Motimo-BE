package kr.co.api.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.UUID;
import kr.co.domain.user.exception.UserNotFoundException;
import kr.co.domain.user.model.InterestType;
import kr.co.domain.user.model.ProviderType;
import kr.co.domain.user.model.Role;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Command 테스트")
class UserCommandServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserCommandService userCommandService;

    @Test
    void 유저_정상_생성() {
        // given
        User testUser = User.builder()
                .email("test@gmail.com")
                .nickname("테스트 사용자")
                .providerType(ProviderType.GOOGLE)
                .role(Role.USER)
                .build();

        when(userRepository.create(any(User.class))).thenReturn(testUser);

        // when
        User savedUser = userCommandService.register(testUser);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@gmail.com");
        assertThat(savedUser.getNickname()).isEqualTo("테스트 사용자");
        assertThat(savedUser.getProviderType()).isEqualTo(ProviderType.GOOGLE);
        assertThat(savedUser.getRole()).isEqualTo(Role.USER);

        verify(userRepository, times(1)).create(testUser);
    }

    @Test
    @DisplayName("Repository 저장 실패 시 예외 전파")
    void repository_저장_실패시_예외_발생() {
        // given
        User userToSave = User.builder()
                .email("test@gmail.com")
                .nickname("테스트 사용자")
                .providerType(ProviderType.LOCAL)
                .build();

        when(userRepository.create(any(User.class)))
                .thenThrow(new RuntimeException("데이터베이스 저장시 오류"));

        // when & then
        assertThatThrownBy(() -> userCommandService.register(userToSave))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("데이터베이스 저장시 오류");

        verify(userRepository, times(1)).create(userToSave);
    }

    @Test
    @DisplayName("유저 관심사 업데이트 성공")
    void 유저_관심사_업데이트_성공() {
        // given
        UUID userId = UUID.randomUUID();
        Set<InterestType> interests = Set.of(InterestType.PROGRAMMING, InterestType.SPORTS);

        User existingUser = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .nickname("테스트 사용자")
                .providerType(ProviderType.GOOGLE)
                .role(Role.USER)
                .build();

        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.update(any(User.class))).thenReturn(existingUser);

        // when
        UUID result = userCommandService.updateInterests(userId, interests);

        // then
        assertThat(result).isEqualTo(userId);
        assertThat(existingUser.getInterests()).isEqualTo(interests);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).update(existingUser);
    }

    @Test
    @DisplayName("존재하지 않는 유저 관심사 업데이트 시 예외 발생")
    void 존재하지_않는_유저_관심사_업데이트시_예외_발생() {
        // given
        UUID nonExistentUserId = UUID.randomUUID();
        Set<InterestType> interests = Set.of(InterestType.PROGRAMMING);

        when(userRepository.findById(nonExistentUserId)).thenThrow(new UserNotFoundException());

        // when & then
        assertThatThrownBy(() -> userCommandService.updateInterests(nonExistentUserId, interests))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, times(1)).findById(nonExistentUserId);
        verify(userRepository, never()).update(any(User.class));
    }

}