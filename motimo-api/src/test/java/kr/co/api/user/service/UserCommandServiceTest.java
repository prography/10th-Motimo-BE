package kr.co.api.user.service;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // when
        User savedUser = userCommandService.register(testUser);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@gmail.com");
        assertThat(savedUser.getNickname()).isEqualTo("테스트 사용자");
        assertThat(savedUser.getProviderType()).isEqualTo(ProviderType.GOOGLE);
        assertThat(savedUser.getRole()).isEqualTo(Role.USER);

        verify(userRepository, times(1)).save(testUser);
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

        when(userRepository.save(any(User.class)))
                .thenThrow(new RuntimeException("데이터베이스 저장시 오류"));

        // when & then
        assertThatThrownBy(() -> userCommandService.register(userToSave))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("데이터베이스 저장시 오류");

        verify(userRepository, times(1)).save(userToSave);
    }

}