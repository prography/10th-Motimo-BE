package kr.co.api.security;

import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomUserDetailsService 테스트")
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    @Test
    @DisplayName("존재하는 이메일이면 UserPrincipal을 반환한다")
    void loadUserByUsername_UserExists_ReturnsUserPrincipal() {
        UUID uuid = UUID.randomUUID();
        String email = "user@example.com";
        String nickname = "test user";
        String password = "encoded_pw";
        User user = new User(uuid, email, nickname, password);

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername(email);

        assertThat(result.getUsername()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(password);
        assertThat(result.getAuthorities().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 이메일이면 예외를 던진다")
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        given(userRepository.findByEmail("notfound@gmail.com")).willReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("notfound@gmail.com"));
    }

}