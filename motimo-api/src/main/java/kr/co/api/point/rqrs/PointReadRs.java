package kr.co.api.point.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;

public record PointReadRs(
    @Schema(description = "사용자가 현재 획득한 포인트", example = "1000")
    int point
) {
}
