package kr.co.api.user.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record UserIdRs(
        @Schema(description = "유저 아이디")
        UUID userId
) {

}
