package kr.co.api.poke;

import java.util.UUID;
import kr.co.api.poke.docs.PokeControllerSwagger;
import kr.co.api.security.annotation.AuthUser;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class PokeController implements PokeControllerSwagger {

    @PostMapping("/groups/members/{targetId}/poke")
    public void sendPokeNotification(@AuthUser UUID userId, @PathVariable UUID targetId) {

    }
}
