package kr.co.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"kr.co"})
public class MotimoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MotimoApiApplication.class, args);
    }

}
