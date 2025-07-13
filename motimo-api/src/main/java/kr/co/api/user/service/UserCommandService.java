package kr.co.api.user.service;

import java.util.Set;
import java.util.UUID;
import kr.co.domain.user.model.InterestType;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;

    public User register(User user) {
        return userRepository.create(user);
    }

    @Transactional
    public UUID updateInterests(UUID userId, Set<InterestType> interests) {
        User user = userRepository.findById(userId);
        user.updateInterests(interests);
        return userRepository.update(user).getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUserLoginAt(UUID userId) {
        userRepository.updateLastLoginAt(userId);
    }

}
