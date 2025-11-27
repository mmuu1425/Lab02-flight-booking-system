package com.example.flightservice.service;

import com.example.flightservice.dto.FlightResponse;
import com.example.flightservice.entity.Flight;
import com.example.flightservice.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Optional<FlightResponse> getFlightByNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber)
                .map(this::convertToFlightResponse);
    }

    public List<FlightResponse> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(this::convertToFlightResponse)
                .collect(Collectors.toList());
    }

    private FlightResponse convertToFlightResponse(Flight flight) {
        FlightResponse response = new FlightResponse();
        response.setFlightNumber(flight.getFlightNumber());

        // 构建机场显示字符串
        if (flight.getFromAirport() != null) {
            String fromAirport = flight.getFromAirport().getCity() + " " + flight.getFromAirport().getName();
            response.setFromAirport(fromAirport);
        }

        if (flight.getToAirport() != null) {
            String toAirport = flight.getToAirport().getCity() + " " + flight.getToAirport().getName();
            response.setToAirport(toAirport);
        }

        // 格式化日期
        if (flight.getDatetime() != null) {
            String formattedDate = flight.getDatetime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            response.setDate(formattedDate);
        }

        response.setPrice(flight.getPrice());

        return response;
    }
}