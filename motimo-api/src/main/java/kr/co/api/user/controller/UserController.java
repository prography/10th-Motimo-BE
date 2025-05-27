package kr.co.api.user.controller;

import kr.co.api.user.controller.rqrs.UserJoinedDateRs;
import kr.co.api.user.docs.UserControllerSwagger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController implements UserControllerSwagger {

    @GetMapping("/joined-dates")
    public UserJoinedDateRs readJoinedDate() {
        return null;
    }
}
