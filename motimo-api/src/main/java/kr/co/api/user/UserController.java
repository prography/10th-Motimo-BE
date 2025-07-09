package kr.co.api.user;

import java.util.UUID;
import kr.co.api.security.annotation.AuthUser;
import kr.co.api.user.docs.UserControllerSwagger;
import kr.co.api.user.rqrs.UserRs;
import kr.co.api.user.service.UserQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController implements UserControllerSwagger {

    private final UserQueryService userQueryService;

    public UserController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping("/me")
    public UserRs getMyProfile(@AuthUser UUID userId) {
        return UserRs.from(userQueryService.findById(userId));
    }

}
