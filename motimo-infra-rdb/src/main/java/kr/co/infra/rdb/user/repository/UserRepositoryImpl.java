package kr.co.infra.rdb.user.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import kr.co.domain.user.exception.UserNotFoundException;
import kr.co.domain.user.model.ProviderType;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import kr.co.infra.rdb.user.entity.UserEntity;
import kr.co.infra.rdb.user.util.UserMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User findById(UUID id) {
        UserEntity userEntity = userJpaRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return UserMapper.toDomain(userEntity);
    }

    @Override
    public User findByEmail(String email) {
        UserEntity userEntity = userJpaRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return UserMapper.toDomain(userEntity);
    }

    @Override
    public User findByEmailAndProviderType(String email, ProviderType providerType) {
        UserEntity userEntity = userJpaRepository.findByEmailAndProviderType(email, providerType)
                .orElseThrow(UserNotFoundException::new);
        return UserMapper.toDomain(userEntity);
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public boolean existsByEmailAndProviderType(String email, ProviderType providerType) {
        return userJpaRepository.existsByEmailAndProviderType(email, providerType);
    }

    @Override
    public List<User> findAllByIdsIn(Set<UUID> userIds) {
        return userJpaRepository.findAllByIdIn(userIds)
                .stream()
                .map(UserMapper::toDomain)
                .toList();
    }
}
