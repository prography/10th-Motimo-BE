package kr.co.api.goal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kr.co.api.goal.docs.GoalControllerSwagger;
import kr.co.api.goal.dto.GoalCreateDto;
import kr.co.api.goal.rqrs.GoalCreateRq;
import kr.co.api.goal.rqrs.GoalIdRs;
import kr.co.api.goal.rqrs.GoalItemRs;
import kr.co.api.goal.rqrs.GoalListRs;
import kr.co.api.goal.rqrs.GoalUpdateRq;
import kr.co.api.goal.service.GoalCommandService;
import kr.co.api.goal.service.GoalQueryService;
import kr.co.api.security.annotation.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PutMapping("/{id}")
    public void updateGoal(@AuthUser UUID userId, @PathVariable String id,
            @RequestBody GoalUpdateRq rq) {
    }

    @GetMapping
    public GoalListRs getGoalList(@AuthUser UUID userId) {
        List<GoalItemRs> items = new ArrayList<>();
        items.add(new GoalItemRs("첫번째 목표", LocalDate.now(), 50));
        items.add(new GoalItemRs("자격증 따기", LocalDate.of(2026, 2, 10), 20));
        items.add(new GoalItemRs("노래 만들기", LocalDate.now(), 100));
        return new GoalListRs(
                items
        );
    }

    @GetMapping("/{goalId}/sub-goals")
    public GoalListRs getSubGoalList(@AuthUser UUID userId, @PathVariable String goalId) {
        List<GoalItemRs> items = new ArrayList<>();
        items.add(new GoalItemRs("첫번째 목표", LocalDate.now(), 50));
        items.add(new GoalItemRs("자격증 따기", LocalDate.of(2026, 2, 10), 20));
        items.add(new GoalItemRs("노래 만들기", LocalDate.now(), 100));
        return new GoalListRs(
                items
        );
    }
}
