package com.flight.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

    @GetMapping("/manage/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/api/bookings/test")
    public String test() {
        return "Booking Service is working with new architecture!";
    }
}