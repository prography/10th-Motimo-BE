package kr.co.domain.user.repository;

import kr.co.domain.user.model.User;

import java.util.UUID;

public interface UserRepository {
    User findById(UUID id);
    User findByEmail(String email);
    User save(User user);
    Boolean existsByEmail(String email);
}
