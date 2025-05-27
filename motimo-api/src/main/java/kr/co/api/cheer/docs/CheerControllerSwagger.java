package kr.co.api.cheer.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.api.cheer.rqrs.PhraseReadRs;

@Tag(name = "응원 API", description = "응원 관련 API 목록입니다")
public interface CheerControllerSwagger {
    @Operation(summary = "응원 문구 조회 API", description = "응원 문구를 랜덤 조회합니다.")
    PhraseReadRs readPhrase();
}
