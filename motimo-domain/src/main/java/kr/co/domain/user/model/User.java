package kr.co.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

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
    private Role role = Role.USER;
    @Builder.Default
    private ProviderType providerType = ProviderType.LOCAL;
    private String providerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void updateProfile(String newNickname, String newImageUrl) {
        if (!Objects.equals(this.nickname, newNickname)) {
            this.nickname = newNickname;
        }
        if (!Objects.equals(this.profileImageUrl, newImageUrl)) {
            this.profileImageUrl = newImageUrl;
        }
    }

}
