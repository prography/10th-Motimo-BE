package kr.co.api.user;

import java.util.List;
import java.util.UUID;
import kr.co.api.security.annotation.AuthUser;
import kr.co.api.todo.rqrs.TodoRs;
import kr.co.api.todo.service.TodoQueryService;
import kr.co.api.user.docs.UserControllerSwagger;
import kr.co.api.user.rqrs.UserIdRs;
import kr.co.api.user.rqrs.UserInterestsRq;
import kr.co.api.user.service.UserCommandService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController implements UserControllerSwagger {

    private final UserCommandService userCommandService;
    private final TodoQueryService todoQueryService;

    public UserController(UserCommandService userCommandService,
            TodoQueryService todoQueryService) {
        this.userCommandService = userCommandService;
        this.todoQueryService = todoQueryService;
    }

    @GetMapping("/me/todos")
    public List<TodoRs> getMyTodos(@AuthUser UUID userId) {
        return todoQueryService.getTodosByUserId(userId)
                .stream()
                .map(TodoRs::from)
                .toList();
    }

    @PutMapping("/interests")
    public UserIdRs updateMyInterests(@AuthUser UUID userId, @RequestBody UserInterestsRq request) {

        UUID id = userCommandService.updateInterests(userId, request.interests());
        return new UserIdRs(id);
    }
}