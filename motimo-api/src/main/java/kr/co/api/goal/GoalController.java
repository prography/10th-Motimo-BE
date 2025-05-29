package kr.co.api.goal;

import kr.co.api.goal.docs.GoalControllerSwagger;
import kr.co.api.goal.rqrs.GoalCreateRq;
import kr.co.api.goal.rqrs.GoalIdRs;
import kr.co.api.goal.rqrs.GoalListRs;
import kr.co.api.goal.rqrs.GoalUpdateRq;
import kr.co.api.security.annotation.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/goals")
public class GoalController implements GoalControllerSwagger {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GoalIdRs create(@RequestBody GoalCreateRq rq) {
        return new GoalIdRs(UUID.randomUUID().toString());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable String id, GoalUpdateRq rq) {
    }

    @GetMapping
    public GoalListRs readList(@AuthUser UUID userId) {
        return null;
    }
}
