package kr.co.api.goal;

import java.util.List;
import java.util.UUID;
import kr.co.api.goal.docs.GoalReadControllerSwagger;
import kr.co.api.goal.dto.GoalItemDto;
import kr.co.api.goal.rqrs.GoalDetailRs;
import kr.co.api.goal.rqrs.GoalItemRs;
import kr.co.api.goal.rqrs.GoalListRs;
import kr.co.api.goal.rqrs.GoalNotInGroupRs;
import kr.co.api.goal.rqrs.GoalWithSubGoalTodoRs;
import kr.co.api.goal.service.GoalQueryService;
import kr.co.api.security.annotation.AuthUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/goals")
public class GoalReadController implements GoalReadControllerSwagger {

    private final GoalQueryService goalQueryService;

    public GoalReadController(final GoalQueryService goalQueryService) {
        this.goalQueryService = goalQueryService;
    }

    @GetMapping
    public GoalListRs getGoalList(@AuthUser UUID userId) {
        List<GoalItemDto> goalList = goalQueryService.getGoalList(userId);
        return new GoalListRs(goalList.stream().map(GoalItemRs::from).toList());
    }

    @GetMapping("/{goalId}")
    public GoalDetailRs getGoalDetail(@PathVariable UUID goalId) {
        return GoalDetailRs.from(goalQueryService.getGoalDetail(goalId));
    }

    @GetMapping("/{goalId}/sub-goals/all")
    public GoalWithSubGoalTodoRs getGoalWithSubGoalAndTodo(@PathVariable UUID goalId) {
        return GoalWithSubGoalTodoRs.from(goalQueryService.getGoalWithIncompleteSubGoalTodayTodos(goalId));
    }

    @GetMapping("/not-joined-group")
    public List<GoalNotInGroupRs> getGoalNotJoinGroup(@AuthUser UUID userId) {
        return goalQueryService.getGoalNotInGroup(userId).stream().map(GoalNotInGroupRs::from).toList();
    }
}
