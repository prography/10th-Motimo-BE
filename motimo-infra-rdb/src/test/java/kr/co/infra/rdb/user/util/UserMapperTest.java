package kr.co.infra.rdb.user.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.UUID;
import kr.co.domain.user.model.User;
import kr.co.infra.rdb.user.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserMapper 테스트")
class UserMapperTest {

    @Test
    void user_entity를_user로_매핑() {
        UUID uuid = UUID.randomUUID();
        UserEntity entity = UserEntity.builder()
                .id(uuid)
                .email("test@gmail.com")
                .nickname("tester")
                .interests(new HashSet<>())
                .build();

        User user = UserMapper.toDomain(entity);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(entity.getId());
        assertThat(user.getEmail()).isEqualTo(entity.getEmail());
        assertThat(user.getNickname()).isEqualTo(entity.getNickname());
    }

    @Test
    void user를_entity로_매핑() {
        UUID uuid = UUID.randomUUID();
        User user = User.builder()
                .id(uuid)
                .email("test@gmail.com")
                .nickname("tester")
                .profileImageUrl("")
                .build();

        UserEntity entity = UserMapper.toEntity(user);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(user.getId());
        assertThat(entity.getEmail()).isEqualTo(user.getEmail());
        assertThat(entity.getNickname()).isEqualTo(user.getNickname());
    }
}