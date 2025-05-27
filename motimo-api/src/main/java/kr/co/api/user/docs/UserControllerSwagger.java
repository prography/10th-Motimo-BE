package kr.co.api.user.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.api.user.controller.rqrs.UserJoinedDateRs;

@Tag(name = "사용자 API", description = "사용자 관련 API 목록입니다")
public interface UserControllerSwagger {
    @Operation(summary = "사용자 가입일 조회 API", description = "사용자 가입일을 조회합니다.")
    UserJoinedDateRs readJoinedDate();
}
