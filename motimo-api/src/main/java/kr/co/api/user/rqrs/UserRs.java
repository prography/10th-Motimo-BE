package kr.co.api.user.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.domain.user.model.User;

public record UserRs(
        @Schema(description = "유저 Id", example = "0197157f-aea4-77bb-8581-3213eb5bd2ae")
        UUID id,
        @Schema(description = "유저 닉네임")
        String nickname,
        @Schema(description = "유저 프로필 이미지 url")
        String profileImageUrl,
        @Schema(description = "유저 생성 날짜", type = "date")
        LocalDateTime createdAt) {

    public static UserRs from(User user) {
        return new UserRs(
                user.getId(), user.getNickname(), user.getProfileImageUrl(), user.getCreatedAt());
    }
}
