package kr.co.api.goal.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import kr.co.api.goal.rqrs.GoalCreateRq;
import kr.co.api.goal.rqrs.GoalIdRs;
import kr.co.api.goal.rqrs.GoalListRs;
import kr.co.api.goal.rqrs.GoalUpdateRq;

@Tag(name = "목표 API", description = "목표 관련 API 목록입니다")
public interface GoalControllerSwagger {

    @Operation(summary = "목표 생성 API", description = "목표를 생성합니다.")
    GoalIdRs createGoal(UUID userId, GoalCreateRq rq);

    @Operation(summary = "목표 수정 API", description = "목표를 수정합니다.")
    void updateGoal(UUID userId, String id, GoalUpdateRq rq);

    @Operation(summary = "목표 목록 API", description = "목표 목록을 조회합니다.")
    GoalListRs getGoalList(UUID userId);
}
