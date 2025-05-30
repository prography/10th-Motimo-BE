package kr.co.infra.rdb.auth.repository;

import kr.co.infra.rdb.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    List<RefreshTokenEntity> findAllByUserId(UUID userId);

    Optional<RefreshTokenEntity> findByTokenId(UUID tokenId);

    void deleteByTokenId(UUID tokenId);

    void deleteAllByUserId(UUID userId);
}
