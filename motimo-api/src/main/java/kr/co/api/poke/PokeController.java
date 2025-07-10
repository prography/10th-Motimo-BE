package kr.co.api.poke;

import java.util.UUID;
import kr.co.api.poke.docs.PokeControllerSwagger;
import kr.co.api.poke.service.PokeCommandService;
import kr.co.api.security.annotation.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class PokeController implements PokeControllerSwagger {
    private final PokeCommandService pokeCommandService;

    public PokeController(final PokeCommandService pokeCommandService) {
        this.pokeCommandService = pokeCommandService;
    }

    @PostMapping("/groups/members/{targetUserId}/poke")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendPokeNotification(@AuthUser UUID userId, @PathVariable UUID targetUserId) {
        pokeCommandService.createPokeNotification(userId, targetUserId);
    }
}
