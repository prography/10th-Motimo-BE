package kr.co.api.subgoal;

import jakarta.validation.Valid;
import java.util.UUID;
import kr.co.api.security.annotation.AuthUser;
import kr.co.api.subgoal.docs.SubGoalControllerSwagger;
import kr.co.api.subgoal.rqrs.SubGoalIdRs;
import kr.co.api.subgoal.rqrs.TodoCreateRq;
import kr.co.api.subgoal.service.SubGoalCommandService;
import kr.co.api.todo.rqrs.TodoRs;
import kr.co.api.todo.service.TodoCommandService;
import kr.co.api.todo.service.TodoQueryService;
import kr.co.domain.common.pagination.CustomSlice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sub-goals")
public class SubGoalController implements SubGoalControllerSwagger {

    private final SubGoalCommandService subGoalCommandService;
    private final TodoCommandService todoCommandService;
    private final TodoQueryService todoQueryService;

    public SubGoalController(TodoCommandService todoCommandService,
            TodoQueryService todoQueryService,
            SubGoalCommandService subGoalCommandService) {
        this.todoCommandService = todoCommandService;
        this.todoQueryService = todoQueryService;
        this.subGoalCommandService = subGoalCommandService;
    }

    @PatchMapping("/{subGoalId}/completion/toggle")
    public SubGoalIdRs subGoalCompleteToggle(@AuthUser UUID userId, @PathVariable UUID subGoalId) {
        return new SubGoalIdRs(subGoalCommandService.toggleSubGoalComplete(userId, subGoalId));
    }

    @PostMapping("/{subGoalId}/todo")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTodo(@AuthUser UUID userId, @PathVariable UUID subGoalId,
            @Valid @RequestBody TodoCreateRq request) {
        todoCommandService.createTodo(userId, subGoalId, request.title(), request.date());
    }

    @GetMapping("/{subGoalId}/todos")
    public CustomSlice<TodoRs> getTodosBySubGoal(@PathVariable UUID subGoalId,
            @RequestParam("page") int page, @RequestParam("size") int size) {
        return todoQueryService.getTodosBySubGoal(subGoalId, page, size).map(TodoRs::from);
    }

}
