package kr.co.domain.user.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import kr.co.domain.user.model.ProviderType;
import kr.co.domain.user.model.User;

public interface UserRepository {

    User findById(UUID id);

    User findByEmail(String email);

    User findByEmailAndProviderType(String email, ProviderType providerType);
    
    User create(User user);

    User update(User user);

    boolean existsByEmailAndProviderType(String email, ProviderType providerType);

    List<User> findAllByIdsIn(Set<UUID> userIds);
}
