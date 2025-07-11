package kr.co.api.user.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.co.api.user.dto.UserProfileDto;

public record UserRs(
        @Schema(description = "유저 Id", example = "0197157f-aea4-77bb-8581-3213eb5bd2ae")
        UUID id,
        @Schema(description = "유저 이메일")
        String email,
        @Schema(description = "유저 닉네임")
        String nickname,
        @Schema(description = "유저 한줄 소개")
        String bio,
        @Schema(description = "유저 프로필 이미지 url")
        String profileImageUrl,
        @Schema(description = "유저 생성 날짜", type = "date")
        LocalDateTime createdAt) {

    public static UserRs from(UserProfileDto dto) {
        return new UserRs(dto.id(), dto.email(), dto.username(), dto.bio(), dto.profileUrl(),
                dto.createdAt());
    }
}
