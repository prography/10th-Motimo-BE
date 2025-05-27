package kr.co.api.point.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.api.point.rqrs.PointReadRs;
import kr.co.api.security.annotation.AuthUser;

import java.util.UUID;

@Tag(name = "포인트 API", description = "포인트 관련 API 목록입니다")
public interface PointControllerSwagger {

    @Operation(summary = "현재 획득한 포인트 조회 API", description = "사용자가 획득한 포인트를 조회합니다.")
    PointReadRs read(@AuthUser UUID userId);
}
