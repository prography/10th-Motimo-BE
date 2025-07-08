package kr.co.domain.user.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class User {

    @Builder.Default
    private UUID id = null;
    private String email;
    private String nickname;
    private String profileImageUrl;
    @Builder.Default
    private Set<InterestType> interests = new HashSet<>();
    @Builder.Default
    private Role role = Role.USER;
    @Builder.Default
    private ProviderType providerType = ProviderType.LOCAL;
    private String providerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(String nickname, String profileImageUrl,
            Set<InterestType> interests) {
        updateProfile(nickname, profileImageUrl);
        updateInterests(interests);
    }

    public void updateInterests(Set<InterestType> newInterests) {
        this.interests = new HashSet<>(newInterests);
    }

    private void updateProfile(String newNickname, String newImageUrl) {
        if (!Objects.equals(this.nickname, newNickname)) {
            this.nickname = newNickname;
        }
        if (!Objects.equals(this.profileImageUrl, newImageUrl)) {
            this.profileImageUrl = newImageUrl;
        }
    }

}
