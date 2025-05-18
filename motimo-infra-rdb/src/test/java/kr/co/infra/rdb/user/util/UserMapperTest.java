package kr.co.infra.rdb.user.util;

import kr.co.domain.user.model.User;
import kr.co.infra.rdb.user.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserMapper 테스트")
class UserMapperTest {

    @Test
    @DisplayName("UserEntity를 User로 매핑")
    void toDomain_ValidEntity_ReturnsUser() {
        UserEntity entity = UserEntity.builder()
                .id(1L)
                .email("test@gmail.com")
                .nickname("tester")
                .password("encoded_pw")
                .build();

        User user = UserMapper.toDomain(entity);

        assertThat(user).isNotNull();
        assertThat(user.id()).isEqualTo(entity.getId());
        assertThat(user.email()).isEqualTo(entity.getEmail());
        assertThat(user.nickname()).isEqualTo(entity.getNickname());
        assertThat(user.password()).isEqualTo(entity.getPassword());
    }

    @Test
    @DisplayName("User를 UserEntity로 매핑")
    void toEntity_ValidUser_ReturnsEntity() {
        User user = new User(1L, "test@gmail.com", "tester", "encoded_pw");

        UserEntity entity = UserMapper.toEntity(user);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(user.id());
        assertThat(entity.getNickname()).isEqualTo(user.nickname());
        assertThat(entity.getPassword()).isEqualTo(user.password());
    }
}