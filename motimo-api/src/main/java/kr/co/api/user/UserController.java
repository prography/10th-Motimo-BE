package kr.co.api.user;

import java.util.UUID;
import kr.co.api.security.annotation.AuthUser;
import kr.co.api.user.docs.UserControllerSwagger;
import kr.co.api.user.rqrs.UserIdRs;
import kr.co.api.user.rqrs.UserInterestsRq;
import kr.co.api.user.rqrs.UserRs;
import kr.co.api.user.rqrs.UserUpdateRq;
import kr.co.api.user.service.UserCommandService;
import kr.co.api.user.service.UserQueryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/users")
public class UserController implements UserControllerSwagger {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UserController(UserCommandService userCommandService,
            UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @GetMapping("/me")
    public UserRs getMyProfile(@AuthUser UUID userId) {
        return UserRs.from(userQueryService.getProfile(userId));
    }

    @GetMapping("/{userId}")
    public UserRs getProfile(@PathVariable UUID userId) {
        return UserRs.from(userQueryService.getProfile(userId));
    }

    @PatchMapping("/interests")
    public UserIdRs updateMyInterests(@AuthUser UUID userId, @RequestBody UserInterestsRq request) {

        UUID id = userCommandService.updateInterests(userId, request.interests());
        return new UserIdRs(id);
    }

    @PutMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserIdRs updateMyProfile(
            @AuthUser UUID userId,
            @RequestPart UserUpdateRq request,
            @RequestPart(name = "file", required = false) MultipartFile image) {
        UUID id = userCommandService.updateProfile(userId,
                request.userName(), request.bio(), request.interests(), image);
        return new UserIdRs(id);
    }

    @DeleteMapping()
    public void deleteUser(@AuthUser UUID userId) {
        userCommandService.deleteUserCascadeById(userId);
    }
}