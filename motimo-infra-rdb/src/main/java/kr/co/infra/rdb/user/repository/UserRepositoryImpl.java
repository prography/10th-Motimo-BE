package kr.co.infra.rdb.user.repository;

import kr.co.domain.user.exception.UserNotFoundException;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import kr.co.infra.rdb.user.entity.UserEntity;
import kr.co.infra.rdb.user.util.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.UUID;

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
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
