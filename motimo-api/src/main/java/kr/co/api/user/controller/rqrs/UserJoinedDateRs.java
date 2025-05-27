package kr.co.api.user.controller.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UserJoinedDateRs(
    @Schema(description = "가입일", type = "date")
    LocalDate date,

    @Schema(description = "가입일로부터 날짜", example = "43")
    int day
) {
}
