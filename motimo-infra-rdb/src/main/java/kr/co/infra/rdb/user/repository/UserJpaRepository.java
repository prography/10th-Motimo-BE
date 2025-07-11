package kr.co.infra.rdb.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import kr.co.domain.user.model.ProviderType;
import kr.co.infra.rdb.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndProviderType(String email, ProviderType providerType);

    boolean existsByEmailAndProviderType(String email, ProviderType providerType);

    List<UserEntity> findAllByIdIn(Set<UUID> userIds);
}
