package kr.co.api.goal.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import kr.co.api.goal.rqrs.GoalCreateRq;
import kr.co.api.goal.rqrs.GoalIdRs;
import kr.co.api.goal.rqrs.GoalListRs;
import kr.co.api.goal.rqrs.GoalTodoListRs;
import kr.co.api.goal.rqrs.GoalUpdateRq;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "목표 API", description = "목표 관련 API 목록입니다")
public interface GoalControllerSwagger {

    @Operation(summary = "목표 생성 API", description = "목표를 생성합니다.")
    GoalIdRs createGoal(UUID userId, GoalCreateRq rq);

    @Operation(summary = "목표 수정 API", description = "목표를 수정합니다.")
    GoalIdRs updateGoal(UUID userId, String id, GoalUpdateRq rq);

    @Operation(summary = "목표 완료 API", description = "목표를 완료합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "완료 처리 성공"),
            @ApiResponse(responseCode = "400", description = "목표 완료 조건이 충족되지 않음"),
            @ApiResponse(responseCode = "403", description = "완료 처리 권한 없음"),
            @ApiResponse(responseCode = "404", description = "목표를 찾을 수 없음")
    })
    GoalIdRs goalComplete(UUID userId, @PathVariable UUID goalId);

    @Operation(summary = "목표 목록 API", description = "목표 목록을 조회합니다.")
    GoalListRs getGoalList(UUID userId);

    @Operation(summary = "목표 투두 목록 API", description = "목표에 해당하는 세부 목표와 투두 목록을 조회합니다.")
    GoalTodoListRs getTodoListByGoal(UUID userId, @PathVariable UUID goalId);
}
