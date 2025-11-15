package com.flight.flight.controller;

import com.flight.flight.dto.FlightDTO;
import com.flight.flight.entity.Flight;
import com.flight.flight.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @PostMapping
    public FlightDTO createFlight(@RequestBody Flight flight) {
        return flightService.createFlight(flight);
    }

    @GetMapping("/number/{flightNumber}")
    public FlightDTO getFlightByNumber(@PathVariable String flightNumber) {
        return flightService.getFlightByNumber(flightNumber);
    }

    @GetMapping("/{flightUid}")
    public FlightDTO getFlightById(@PathVariable UUID flightUid) {
        return flightService.getFlightById(flightUid);
    }

    @GetMapping
    public List<FlightDTO> getAllFlights() {
        return flightService.getAllFlights();
    }

    @GetMapping("/search")
    public List<FlightDTO> searchFlights(@RequestParam String from, @RequestParam String to) {
        return flightService.searchFlights(from, to);
    }

    @PutMapping("/{flightUid}")
    public FlightDTO updateFlight(@PathVariable UUID flightUid, @RequestBody Flight flightDetails) {
        return flightService.updateFlight(flightUid, flightDetails);
    }

    @PatchMapping("/{flightUid}/seats")
    public FlightDTO updateSeats(@PathVariable UUID flightUid, @RequestParam Integer seats) {
        return flightService.updateSeats(flightUid, seats);
    }

    @DeleteMapping("/{flightUid}")
    public String deleteFlight(@PathVariable UUID flightUid) {
        flightService.deleteFlight(flightUid);
        return "Flight deleted successfully";
    }

    @PatchMapping("/{flightUid}/reduce-seats")
    public FlightDTO reduceSeats(@PathVariable UUID flightUid, @RequestParam Integer count) {
        return flightService.reduceSeats(flightUid, count);
    }

    @PatchMapping("/{flightUid}/add-seats")
    public FlightDTO addSeats(@PathVariable UUID flightUid, @RequestParam Integer count) {
        return flightService.addSeats(flightUid, count);
    }
}