package kr.co.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"kr.co"})
@EnableScheduling
public class MotimoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MotimoApiApplication.class, args);
    }

}
