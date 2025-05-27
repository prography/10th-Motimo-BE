package kr.co.api.goal;

import kr.co.api.common.rs.IdRs;
import kr.co.api.goal.docs.GoalControllerSwagger;
import kr.co.api.goal.rqrs.GoalCreateRq;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/goals")
public class GoalController implements GoalControllerSwagger {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public IdRs create(@RequestBody GoalCreateRq rq) {
        System.out.println("ddd"+rq.subGoals());
        return null;
    }
}
