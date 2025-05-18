package kr.co.api.security;

import kr.co.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;

class UserPrincipalTest {

    @Test
    @DisplayName("User로부터 UserPrincipal을 생성한다")
    void of_ValidUser_ReturnsCorrectUserPrincipal() {
        User user = new User(1L, "test@gmail.com", "tester", "encoded_pw");

        UserPrincipal principal = UserPrincipal.of(user);

        assertThat(principal.getUsername()).isEqualTo("test@gmail.com");
        assertThat(principal.getPassword()).isEqualTo("encoded_pw");
        assertThat(principal.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
    }
}