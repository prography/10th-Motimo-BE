package kr.co.api.goal;

import kr.co.api.goal.docs.GoalControllerSwagger;
import kr.co.api.goal.rqrs.*;
import kr.co.api.security.annotation.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/goals")
public class GoalController implements GoalControllerSwagger {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GoalIdRs createGoal(@RequestBody GoalCreateRq rq) {
        return new GoalIdRs(UUID.randomUUID().toString());
    }

    @PutMapping("/{id}")
    public void updateGoal(@PathVariable String id, GoalUpdateRq rq) {
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
}
