package kr.co.api.goal.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.api.common.rs.IdRs;
import kr.co.api.goal.rqrs.GoalCreateRq;

@Tag(name = "목표 API", description = "목표 관련 API 목록입니다")
public interface GoalControllerSwagger {
    @Operation(summary = "목표 생성 API", description = "목표를 생성합니다.")
    IdRs create(GoalCreateRq rq);
}
