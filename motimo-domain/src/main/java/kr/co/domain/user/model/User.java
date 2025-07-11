package kr.co.domain.user.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Builder
@AllArgsConstructor
public class User {

    @Builder.Default
    private UUID id = null;
    private String email;
    private String nickname;
    @Builder.Default
    private String profileImageUrl = "";
    @Builder.Default
    private String bio = "";
    @Builder.Default
    private Set<InterestType> interests = new HashSet<>();
    @Builder.Default
    private Role role = Role.USER;
    @Builder.Default
    private ProviderType providerType = ProviderType.LOCAL;
    private String providerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(String nickname, String bio, String profileImageUrl,
            Set<InterestType> interests) {
        updateProfile(nickname, bio, profileImageUrl);
        updateInterests(interests);
    }

    public void updateInterests(Set<InterestType> interests) {
        if (interests == null) {
            this.interests = new HashSet<>();
            return;
        }
        this.interests = new HashSet<>(interests);
    }

    private void updateProfile(String newNickname, String newBio, String newProfileImageUrl) {
        if (StringUtils.hasText(newNickname) && !Objects.equals(this.nickname, newNickname)) {
            this.nickname = newNickname;
        }

        if (!Objects.equals(this.bio, newBio)) {
            this.bio = newBio;
        }

        if (!Objects.equals(this.profileImageUrl, newProfileImageUrl)) {
            this.profileImageUrl = newProfileImageUrl;
        }
    }

}
