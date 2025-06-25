package kr.co.api.goal;

import java.util.UUID;
import kr.co.api.goal.docs.GoalControllerSwagger;
import kr.co.api.goal.dto.GoalCreateDto;
import kr.co.api.goal.dto.SubGoalCreateDto;
import kr.co.api.goal.rqrs.GoalCreateRq;
import kr.co.api.goal.rqrs.GoalIdRs;
import kr.co.api.goal.rqrs.GoalUpdateRq;
import kr.co.api.goal.rqrs.SubGoalCreateRq;
import kr.co.api.goal.service.GoalCommandService;
import kr.co.api.security.annotation.AuthUser;
import kr.co.api.subgoal.rqrs.SubGoalIdRs;
import kr.co.api.subgoal.service.SubGoalCommandService;
import org.springframework.http.HttpStatus;
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
    private final SubGoalCommandService subGoalCommandService;

    public GoalController(final GoalCommandService goalCommandService,
            final SubGoalCommandService subGoalCommandService) {
        this.goalCommandService = goalCommandService;
        this.subGoalCommandService = subGoalCommandService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GoalIdRs createGoal(@AuthUser UUID userId, @RequestBody GoalCreateRq rq) {
        return new GoalIdRs(goalCommandService.createGoal(userId, GoalCreateDto.from(rq)));
    }

    @PutMapping("/{goalId}")
    public GoalIdRs updateGoal(@AuthUser UUID userId, @PathVariable UUID goalId,
            @RequestBody GoalUpdateRq rq) {
//        goalCommandService.updateGoal(userId, goalId, new GoalUpdateDto());
        return null;
    }

    @PostMapping("/{goalId}/subGoals")
    public SubGoalIdRs addSubGoal(@AuthUser UUID userId, @PathVariable UUID goalId,
            @RequestBody SubGoalCreateRq rq) {
        return new SubGoalIdRs(subGoalCommandService.createSubGoal(userId,goalId, SubGoalCreateDto.from(rq)));
    }

    @PatchMapping("/{goalId}/completion")
    public GoalIdRs goalComplete(@AuthUser UUID userId, @PathVariable UUID goalId) {
        return new GoalIdRs(goalCommandService.completeGoal(userId, goalId));
    }

}
