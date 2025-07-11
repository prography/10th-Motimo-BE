package kr.co.api.user.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import kr.co.domain.user.model.InterestType;
import kr.co.domain.user.model.User;

public record UserProfileDto(
        UUID id,
        String email,
        String username,
        String bio,
        String profileUrl,
        Set<InterestType> interests,
        LocalDateTime createdAt
) {

    public static UserProfileDto of(User user, String profileUrl) {
        return new UserProfileDto(
                user.getId(), user.getEmail(), user.getNickname(), user.getBio(), profileUrl,
                user.getInterests(), user.getCreatedAt()
        );
    }

}
