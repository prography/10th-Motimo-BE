package kr.co.api.user.controller;

import kr.co.api.user.controller.rqrs.UserJoinedDateRs;
import kr.co.api.user.docs.UserControllerSwagger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/v1/users")
public class UserController implements UserControllerSwagger {

    @GetMapping("/me/joined-date")
    public UserJoinedDateRs readJoinedDate() {
        LocalDate joinedDate = LocalDate.of(2025, 5, 20);
        return new UserJoinedDateRs(joinedDate, (int) ChronoUnit.DAYS.between(joinedDate, LocalDate.now()));
    }
}
