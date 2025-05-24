package kr.co.api.security;

import kr.co.api.user.service.UserQueryService;
import kr.co.domain.user.exception.UserNotFoundException;
import kr.co.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomUserDetailsService 테스트")
class CustomUserDetailsServiceTest {

    @Mock
    private UserQueryService userQueryService;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("이메일로 유저를 정상적으로 조회하면 UserDetails를 반환한다")
    void loadUserByUsername_UserExists_ReturnsUserPrincipal() {
        UUID userId = UUID.randomUUID();
        String email = "test@gmail.com";
        String nickname = "test user";
        User user = User.builder()
                .id(userId)
                .email(email)
                .nickname(nickname)
                .build();

        given(userQueryService.findByEmail(email)).willReturn(user);

        UserDetails result = customUserDetailsService.loadUserByUsername(email);

        assertThat(result.getUsername()).isEqualTo(email);
        assertThat(result.getAuthorities().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("이메일로 유저를 찾지 못하면 UsernameNotFoundException을 던진다")
    void loadUserByUsername_notFound() {
        // given
        String email = "notfound@example.com";
        when(userQueryService.findByEmail(email)).thenThrow(new UserNotFoundException());

        // when & then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(email))
                .isInstanceOf(UserNotFoundException.class);
        verify(userQueryService).findByEmail(email);
    }

    @Test
    @DisplayName("ID로 유저를 정상적으로 조회하면 UserDetails를 반환한다")
    void loadUserById_success() {
        // given
        UUID userId = UUID.randomUUID();
        String email = "test@gmail.com";
        String nickname = "test user";
        User user = User.builder()
                .id(userId)
                .email(email)
                .nickname(nickname)
                .build();
        when(userQueryService.findById(userId)).thenReturn(user);

        // when
        UserDetails userDetails = customUserDetailsService.loadUserById(userId);

        // then
        verify(userQueryService).findById(userId);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(email);
    }

    @Test
    @DisplayName("ID로 유저를 찾지 못하면 예외를 던진다")
    void loadUserById_notFound() {
        // given
        UUID userId = UUID.randomUUID();
        when(userQueryService.findById(userId)).thenThrow(new UserNotFoundException());

        // when & then
        assertThatThrownBy(() -> customUserDetailsService.loadUserById(userId))
                .isInstanceOf(UserNotFoundException.class);
        verify(userQueryService).findById(userId);
    }

}