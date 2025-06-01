package kr.co.api.todo;

import jakarta.validation.Valid;
import java.util.UUID;
import kr.co.api.security.annotation.AuthUser;
import kr.co.api.todo.docs.TodoControllerSwagger;
import kr.co.api.todo.rqrs.TodoCreateRq;
import kr.co.api.todo.rqrs.TodoResultRq;
import kr.co.api.todo.rqrs.TodoRs;
import kr.co.api.todo.service.TodoCommandService;
import kr.co.api.todo.service.TodoQueryService;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.todo.Todo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/todos")
public class TodoController implements TodoControllerSwagger {

    private final TodoCommandService todoCommandService;
    private final TodoQueryService todoQueryService;

    public TodoController(TodoCommandService todoCommandService,
            TodoQueryService todoQueryService) {
        this.todoCommandService = todoCommandService;
        this.todoQueryService = todoQueryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTodo(@AuthUser UUID userId, @Valid @RequestBody TodoCreateRq request) {
        todoCommandService.createTodo(userId, request.subGoalId(), request.title(), request.date());
    }

    @PostMapping(value = "/{id}/result", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void submitResult(@AuthUser UUID userId,
            @PathVariable UUID id,
            @RequestBody @Valid TodoResultRq request,
            @RequestPart(required = false) MultipartFile file) {
        todoCommandService.submitTodoResult(userId, id, request.emotion(), request.content(), file);
    }

    @GetMapping("/{id}")
    public TodoRs getTodoDetail(@PathVariable UUID id) {
        Todo todo = todoQueryService.getTodo(id);
        // todo: todo result도 포함하도록 수정.
        return TodoRs.of(todo);
    }

    @GetMapping("/sub-goal/{subGoalId}")
    public CustomSlice<TodoRs> getTodosBySubGoal(@PathVariable UUID subGoalId,
            @RequestParam("page") int page, @RequestParam("size") int size) {
        return todoQueryService.getTodosBySubGoal(subGoalId, page, size).map(TodoRs::of);
    }

    @GetMapping("/me")
    public CustomSlice<TodoRs> getMyTodos(@AuthUser UUID userId, @RequestParam("page") int page,
            @RequestParam("size") int size) {
        return todoQueryService.getTodosByUser(userId, page, size).map(TodoRs::of);
    }

    @PatchMapping("/{id}/result/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelResult(@AuthUser UUID userId, @PathVariable UUID id) {
        todoCommandService.cancelTodoResult(userId, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@AuthUser UUID userId, @PathVariable UUID id) {
        todoCommandService.deleteById(userId, id);
    }
    // todo: 투두 결과 수정 기능 추가
    // todo: 투두 내용 수정 기능 추가
}
