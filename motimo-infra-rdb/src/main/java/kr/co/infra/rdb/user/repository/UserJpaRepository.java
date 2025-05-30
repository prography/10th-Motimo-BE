package kr.co.infra.rdb.user.repository;

import kr.co.domain.user.model.ProviderType;
import kr.co.infra.rdb.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndProviderType(String email, ProviderType providerType);

    boolean existsByEmailAndProviderType(String email, ProviderType providerType);
}
