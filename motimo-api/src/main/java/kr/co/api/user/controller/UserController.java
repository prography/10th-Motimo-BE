package kr.co.api.user.controller;

import kr.co.api.user.docs.UserControllerSwagger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController implements UserControllerSwagger {

}
