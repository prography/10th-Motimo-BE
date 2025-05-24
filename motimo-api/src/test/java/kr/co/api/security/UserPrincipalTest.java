package kr.co.api.security;

import kr.co.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserPrincipalTest {

    @Test
    @DisplayName("User로부터 UserPrincipal을 생성한다")
    void create_ValidUser_ReturnsCorrectUserPrincipal() {
        UUID uuid = UUID.randomUUID();
        String email = "test@gmail.com";
        String nickname = "test user";
        User user = User.builder()
                .id(uuid)
                .email(email)
                .nickname(nickname)
                .build();

        UserPrincipal principal = UserPrincipal.create(user);

        assertThat(principal.getUsername()).isEqualTo("test@gmail.com");
        assertThat(principal.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
    }
}