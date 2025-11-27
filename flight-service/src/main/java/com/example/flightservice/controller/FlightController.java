package com.example.flightservice.controller;

import com.example.flightservice.entity.Flight;
import com.example.flightservice.repository.FlightRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class FlightController {

    private final FlightRepository flightRepository;

    public FlightController(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    // 统一的健康检查（根路径）
    @GetMapping("/flights/manage/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    // 航班API（有路径前缀）
    @GetMapping("/flights")
    public ResponseEntity<Page<Flight>> getFlights(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Flight> flights = flightRepository.findAll(pageable);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/flights/{flightNumber}")
    public ResponseEntity<Flight> getFlightByNumber(@PathVariable String flightNumber) {
        Optional<Flight> flight = flightRepository.findByFlightNumber(flightNumber);
        return flight.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}