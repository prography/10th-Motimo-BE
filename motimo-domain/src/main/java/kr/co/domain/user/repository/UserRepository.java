package kr.co.domain.user.repository;

import kr.co.domain.user.model.User;

public interface UserRepository {
    User findById(Long id);
    User findByEmail(String email);
    User save(User user);
    Boolean existsByEmail(String email);
}
