package kr.co.api.goal;

import java.util.List;
import java.util.UUID;
import kr.co.api.goal.docs.GoalControllerSwagger;
import kr.co.api.goal.dto.GoalCreateDto;
import kr.co.api.goal.dto.GoalItemDto;
import kr.co.api.goal.rqrs.GoalCreateRq;
import kr.co.api.goal.rqrs.GoalDetailRs;
import kr.co.api.goal.rqrs.GoalIdRs;
import kr.co.api.goal.rqrs.GoalItemRs;
import kr.co.api.goal.rqrs.GoalListRs;
import kr.co.api.goal.rqrs.GoalUpdateRq;
import kr.co.api.goal.rqrs.GoalWithSubGoalTodoRs;
import kr.co.api.goal.service.GoalCommandService;
import kr.co.api.goal.service.GoalQueryService;
import kr.co.api.security.annotation.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/goals")
public class GoalController implements GoalControllerSwagger {

    private final GoalCommandService goalCommandService;
    private final GoalQueryService goalQueryService;

    public GoalController(final GoalCommandService goalCommandService,
            final GoalQueryService goalQueryService) {
        this.goalCommandService = goalCommandService;
        this.goalQueryService = goalQueryService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GoalIdRs createGoal(@AuthUser UUID userId, @RequestBody GoalCreateRq rq) {
        return new GoalIdRs(goalCommandService.createGoal(userId, GoalCreateDto.from(rq)));
    }

    @PutMapping("/{goalId}")
    public GoalIdRs updateGoal(@AuthUser UUID userId, @PathVariable String goalId,
            @RequestBody GoalUpdateRq rq) {
//        goalCommandService.updateGoal(userId, goalId, new GoalUpdateDto());
        return null;
    }

    @PatchMapping("/{goalId}/completion")
    public GoalIdRs goalComplete(@AuthUser UUID userId, @PathVariable UUID goalId) {
        return new GoalIdRs(goalCommandService.completeGoal(userId, goalId));
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
    public GoalWithSubGoalTodoRs getTodoListByGoal(@PathVariable UUID goalId) {
        return GoalWithSubGoalTodoRs.from(goalQueryService.getGoalWithIncompleteSubGoalTodayTodos(goalId));
    }

}
