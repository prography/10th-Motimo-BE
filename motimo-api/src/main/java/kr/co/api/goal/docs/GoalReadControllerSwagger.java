package kr.co.api.goal.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import kr.co.api.goal.rqrs.CompletedGoalListRs;
import kr.co.api.goal.rqrs.GoalDetailRs;
import kr.co.api.goal.rqrs.GoalListRs;
import kr.co.api.goal.rqrs.GoalNotInGroupRs;
import kr.co.api.goal.rqrs.GoalWithSubGoalRs;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "목표 API", description = "목표 관련 API 목록입니다")
public interface GoalReadControllerSwagger {

    @Operation(summary = "목표 상세 API", description = "목표 상세 정보를 조회합니다.")
    GoalDetailRs getGoalDetail(@PathVariable UUID goalId);

    @Operation(summary = "목표 목록 API", description = "목표 목록을 조회합니다.")
    GoalListRs getGoalList(UUID userId);

    @Operation(summary = "완료된 목표 목록 조회 API", description = "사용자의 완료된 목표 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "완료된 목표 목록과 각 목표별 투두 개수, 투두 결과 개수 반환", content = @Content(schema = @Schema(implementation = CompletedGoalListRs.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
    })
    CompletedGoalListRs getCompletedGoals(UUID userId);

    @Operation(summary = "목표와 세부목표 목록 API", description = "목표와 세부 목표 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "목표아이디에 해당하는 모든 세부 목표목록을 반환", content = @Content(schema = @Schema(implementation = GoalWithSubGoalRs.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
    })
    GoalWithSubGoalRs getGoalWithSubGoal(@PathVariable UUID goalId);

    @Operation(summary = "그룹에 참여하지 않은 목표 목록 API", description = "그룹에 참여하지 않은 목표를 조회합니다.")
    List<GoalNotInGroupRs> getGoalNotJoinGroup(UUID userId);
}
