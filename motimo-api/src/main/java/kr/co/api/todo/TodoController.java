package kr.co.api.todo;

import java.util.UUID;
import kr.co.api.security.annotation.AuthUser;
import kr.co.api.todo.docs.TodoControllerSwagger;
import kr.co.api.todo.rqrs.TodoIdRs;
import kr.co.api.todo.rqrs.TodoResultIdRs;
import kr.co.api.todo.rqrs.TodoResultRq;
import kr.co.api.todo.rqrs.TodoResultRs;
import kr.co.api.todo.rqrs.TodoUpdateRq;
import kr.co.api.todo.service.TodoCommandService;
import kr.co.api.todo.service.TodoQueryService;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
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
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResultIdRs submitResult(@AuthUser UUID userId,
            @PathVariable UUID todoId,
            @RequestPart TodoResultRq request,
            @RequestPart(name = "file", required = false) MultipartFile file) {
        TodoResult todoResult = todoCommandService.submitTodoResult(
                userId, todoId, request.emotion(), request.content(), file);

        return new TodoResultIdRs(todoResult.getId());
    }

    @GetMapping("/{todoId}/result")
    public ResponseEntity<TodoResultRs> getTodoResult(@PathVariable UUID todoId) {
        return todoQueryService.getTodoResultByTodoId(todoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PatchMapping("/{todoId}/completion")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public TodoIdRs toggleTodoCompletion(@AuthUser UUID userId, @PathVariable UUID todoId) {
        Todo todo = todoCommandService.toggleTodoCompletion(userId, todoId);
        return new TodoIdRs(todo.getId());
    }

    @PutMapping("/{todoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public TodoIdRs updateTodo(@AuthUser UUID userId, @PathVariable UUID todoId,
            @RequestBody TodoUpdateRq request) {
        Todo todo = todoCommandService.updateTodo(userId, todoId, request.title(), request.date());
        return new TodoIdRs(todo.getId());
    }

    @DeleteMapping("/{todoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@AuthUser UUID userId, @PathVariable UUID todoId) {
        todoCommandService.deleteById(userId, todoId);
    }

    @DeleteMapping("/{todoId}/result")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodoResultByTodoId(@AuthUser UUID userId, @PathVariable UUID todoId) {
        todoCommandService.deleteTodoResultByTodoId(userId, todoId);
    }

    // todo: 투두 결과 수정 기능 추가
}
