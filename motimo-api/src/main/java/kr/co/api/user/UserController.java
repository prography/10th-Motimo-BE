package kr.co.api.user;

import java.util.UUID;
import kr.co.api.security.annotation.AuthUser;
import kr.co.api.todo.rqrs.TodoRs;
import kr.co.api.todo.service.TodoQueryService;
import kr.co.api.user.docs.UserControllerSwagger;
import kr.co.domain.common.pagination.CustomSlice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController implements UserControllerSwagger {

    private final TodoQueryService todoQueryService;

    public UserController(TodoQueryService todoQueryService) {
        this.todoQueryService = todoQueryService;
    }

    @GetMapping("/me/todos")
    public CustomSlice<TodoRs> getMyTodos(@AuthUser UUID userId, @RequestParam("page") int page,
            @RequestParam("size") int size) {
        return todoQueryService.getTodosByUser(userId, page, size).map(TodoRs::of);
    }
}
