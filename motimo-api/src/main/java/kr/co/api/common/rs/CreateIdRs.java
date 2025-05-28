package kr.co.api.common.rs;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateIdRs(
    @Schema(description = "생성된 아이디")
    String id
) {
}
