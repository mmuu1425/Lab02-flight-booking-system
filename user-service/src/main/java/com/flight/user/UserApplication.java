package com.flight.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @GetMapping("/manage/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/api/users/test")
    public String test() {
        return "User Service is working with new architecture!";
    }
}