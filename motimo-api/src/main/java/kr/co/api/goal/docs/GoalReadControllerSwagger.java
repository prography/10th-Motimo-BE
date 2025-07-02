package kr.co.api.goal.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import kr.co.api.goal.rqrs.GoalDetailRs;
import kr.co.api.goal.rqrs.GoalListRs;
import kr.co.api.goal.rqrs.GoalNotInGroupRs;
import kr.co.api.goal.rqrs.GoalWithSubGoalTodoRs;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "목표 API", description = "목표 관련 API 목록입니다")
public interface GoalReadControllerSwagger {
    @Operation(summary = "목표 상세 API", description = "목표 상세 정보를 조회합니다.")
    GoalDetailRs getGoalDetail(@PathVariable UUID goalId);

    @Operation(summary = "목표 목록 API", description = "목표 목록을 조회합니다.")
    GoalListRs getGoalList(UUID userId);

    @Operation(summary = "목표 투두 목록 API", description = "목표에 해당하는 세부 목표와 투두 목록을 조회합니다.")
    GoalWithSubGoalTodoRs getGoalWithSubGoalAndTodo(@PathVariable UUID goalId);

    @Operation(summary = "그룹에 참여하지 않은 목표 목록 API", description = "그룹에 참여하지 않은 목표를 조회합니다.")
    List<GoalNotInGroupRs> getGoalNotJoinGroup(UUID userId);
}
