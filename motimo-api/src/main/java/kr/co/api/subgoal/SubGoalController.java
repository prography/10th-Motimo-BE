package kr.co.api.subgoal;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import kr.co.api.security.annotation.AuthUser;
import kr.co.api.subgoal.docs.SubGoalControllerSwagger;
import kr.co.api.subgoal.rqrs.TodoCreateRq;
import kr.co.api.todo.rqrs.TodoIdRs;
import kr.co.api.todo.rqrs.TodoRs;
import kr.co.api.todo.service.TodoCommandService;
import kr.co.api.todo.service.TodoQueryService;
import kr.co.domain.todo.Todo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sub-goals")
public class SubGoalController implements SubGoalControllerSwagger {

    private final TodoCommandService todoCommandService;
    private final TodoQueryService todoQueryService;

    public SubGoalController(TodoCommandService todoCommandService,
            TodoQueryService todoQueryService) {
        this.todoCommandService = todoCommandService;
        this.todoQueryService = todoQueryService;
    }

    @PostMapping("/{subGoalId}/todo")
    @ResponseStatus(HttpStatus.CREATED)
    public TodoIdRs createTodo(@AuthUser UUID userId, @PathVariable UUID subGoalId,
            @Valid @RequestBody TodoCreateRq request) {
        Todo todo = todoCommandService.createTodo(userId, subGoalId, request.title(),
                request.date());
        return new TodoIdRs(todo.getId());
    }

    @GetMapping("/{subGoalId}/todos/incomplete-or-date")
    public List<TodoRs> getIncompleteOrTodayTodos(@PathVariable UUID subGoalId) {
        return todoQueryService.getIncompleteOrTodayTodosBySubGoalId(subGoalId)
                .stream()
                .map(TodoRs::from)
                .toList();
    }
}
