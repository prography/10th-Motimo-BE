package kr.co.api.user;

import java.util.UUID;
import kr.co.api.security.annotation.AuthUser;
import kr.co.api.user.docs.UserControllerSwagger;
import kr.co.api.user.rqrs.UserRs;
import kr.co.api.user.service.UserQueryService;
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
    private final UserQueryService userQueryService;

    public UserController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @GetMapping("/me")
    public UserRs getMyProfile(@AuthUser UUID userId) {
        return UserRs.from(userQueryService.findById(userId));
    }

    @PutMapping("/interests")
    public UserIdRs updateMyInterests(@AuthUser UUID userId, @RequestBody UserInterestsRq request) {

        UUID id = userCommandService.updateInterests(userId, request.interests());
        return new UserIdRs(id);
    }
}