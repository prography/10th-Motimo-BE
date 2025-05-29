package kr.co.api.point;

import kr.co.api.point.docs.PointControllerSwagger;
import kr.co.api.point.rqrs.PointRs;
import kr.co.api.security.annotation.AuthUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/points")
public class PointController implements PointControllerSwagger {

    @GetMapping
    public PointRs read(@AuthUser UUID userId) {
        return new PointRs(3000);
    }
}
