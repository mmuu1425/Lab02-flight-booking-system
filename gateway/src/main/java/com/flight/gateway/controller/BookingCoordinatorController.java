package com.flight.gateway.controller;

import com.flight.gateway.dto.BookingRequest;
import com.flight.gateway.dto.CompleteBookingResponse;
import com.flight.gateway.service.BookingCoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bookings")
public class BookingCoordinatorController {

    @Autowired
    private BookingCoordinatorService bookingCoordinatorService;

    // 完整的航班预订流程协调
    @PostMapping("/complete")
    public Mono<ResponseEntity<CompleteBookingResponse>> createCompleteBooking(@RequestBody BookingRequest request) {
        return bookingCoordinatorService.createCompleteBooking(request.getUsername(), request.getFlightNumber())
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.badRequest().body(response);
                    }
                });
    }

    // 查询参数版本的预订流程
    @PostMapping("/complete-by-params")
    public Mono<ResponseEntity<CompleteBookingResponse>> createCompleteBookingByParams(
            @RequestParam String username,
            @RequestParam String flightNumber) {
        return bookingCoordinatorService.createCompleteBooking(username, flightNumber)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.badRequest().body(response);
                    }
                });
    }
}