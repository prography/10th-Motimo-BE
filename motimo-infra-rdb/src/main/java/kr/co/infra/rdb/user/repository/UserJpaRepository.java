package kr.co.infra.rdb.user.repository;

import kr.co.infra.rdb.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
