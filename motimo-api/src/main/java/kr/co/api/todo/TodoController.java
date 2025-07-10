package kr.co.api.todo;

import java.util.List;
import java.util.UUID;
import kr.co.api.security.annotation.AuthUser;
import kr.co.api.todo.docs.TodoControllerSwagger;
import kr.co.api.todo.rqrs.TodoIdRs;
import kr.co.api.todo.rqrs.TodoResultIdRs;
import kr.co.api.todo.rqrs.TodoResultRq;
import kr.co.api.todo.rqrs.TodoResultRs;
import kr.co.api.todo.rqrs.TodoRs;
import kr.co.api.todo.rqrs.TodoUpdateRq;
import kr.co.api.todo.service.TodoCommandService;
import kr.co.api.todo.service.TodoQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping(path = "/{todoId}/result", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TodoResultIdRs upsertTodoResult(@AuthUser UUID userId,
            @PathVariable UUID todoId,
            @RequestPart TodoResultRq request,
            @RequestPart(name = "file", required = false) MultipartFile file) {
        UUID id = todoCommandService.upsertTodoResult(
                userId, todoId, request.emotion(), request.content(), file);

        return new TodoResultIdRs(id);
    }

    @GetMapping("/me")
    public List<TodoRs> getMyTodos(@AuthUser UUID userId) {
        return todoQueryService.getTodosByUserId(userId)
                .stream()
                .map(TodoRs::from)
                .toList();
    }

    @GetMapping("/{todoId}/result")
    public ResponseEntity<TodoResultRs> getTodoResult(@PathVariable UUID todoId) {
        return todoQueryService.getTodoResultByTodoId(todoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PatchMapping("/{todoId}/completion")
    public TodoIdRs toggleTodoCompletion(@AuthUser UUID userId, @PathVariable UUID todoId) {
        UUID id = todoCommandService.toggleTodoCompletion(userId, todoId);
        return new TodoIdRs(id);
    }

    @PutMapping("/{todoId}")
    public TodoIdRs updateTodo(@AuthUser UUID userId, @PathVariable UUID todoId,
            @RequestBody TodoUpdateRq request) {
        UUID id = todoCommandService.updateTodo(userId, todoId, request.title(), request.date());
        return new TodoIdRs(id);
    }

    @DeleteMapping("/{todoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@AuthUser UUID userId, @PathVariable UUID todoId) {
        todoCommandService.deleteById(userId, todoId);
    }

    @DeleteMapping("/result/{todoResultId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodoResultById(@AuthUser UUID userId, @PathVariable UUID todoResultId) {
        todoCommandService.deleteTodoResultById(userId, todoResultId);
    }
}
