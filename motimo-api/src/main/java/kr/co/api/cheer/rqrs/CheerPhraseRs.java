package kr.co.api.cheer.rqrs;

import io.swagger.v3.oas.annotations.media.Schema;

public record CheerPhraseRs(
        @Schema(description = "응원 문구", example = "목표는 멀어도 나는 계속 가는중")
        String cheerPhrase
) {

}
