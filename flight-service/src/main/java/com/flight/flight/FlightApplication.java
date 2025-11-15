package com.flight.flight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class FlightApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightApplication.class, args);
    }

    @GetMapping("/manage/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/api/flights/test")
    public String test() {
        return "Flight Service is working with new architecture!";
    }
}