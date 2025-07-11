package kr.co.api.user.service;

import java.util.Set;
import java.util.UUID;
import kr.co.domain.user.model.InterestType;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;

    public User register(User user) {
        return userRepository.create(user);
    }

    public UUID updateInterests(UUID userId, Set<InterestType> interests) {
        User user = userRepository.findById(userId);
        user.updateInterests(interests);
        return userRepository.update(user).getId();
    }

}
