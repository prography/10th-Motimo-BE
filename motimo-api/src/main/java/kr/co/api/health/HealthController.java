package kr.co.api.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
