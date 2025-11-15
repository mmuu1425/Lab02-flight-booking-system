package com.flight.flight.service;

import com.flight.flight.dto.FlightDTO;
import com.flight.flight.entity.Flight;
import com.flight.flight.repository.FlightRepository;
import com.flight.flight.exception.FlightNotFoundException;
import com.flight.flight.exception.InvalidSeatOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    public FlightDTO createFlight(Flight flight) {
        Flight savedFlight = flightRepository.save(flight);
        return convertToDTO(savedFlight);
    }

    public FlightDTO getFlightByNumber(String flightNumber) {
        Flight flight = flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found: " + flightNumber));
        return convertToDTO(flight);
    }

    public FlightDTO getFlightById(UUID flightUid) {
        Flight flight = flightRepository.findById(flightUid)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found: " + flightUid));
        return convertToDTO(flight);
    }

    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FlightDTO> searchFlights(String from, String to) {
        return flightRepository.findAll().stream()
                .filter(flight -> flight.getDeparture().equalsIgnoreCase(from)
                        && flight.getDestination().equalsIgnoreCase(to))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FlightDTO updateFlight(UUID flightUid, Flight flightDetails) {
        Flight flight = flightRepository.findById(flightUid)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found: " + flightUid));

        if (flightDetails.getFlightNumber() != null) {
            flight.setFlightNumber(flightDetails.getFlightNumber());
        }
        if (flightDetails.getDeparture() != null) {
            flight.setDeparture(flightDetails.getDeparture());
        }
        if (flightDetails.getDestination() != null) {
            flight.setDestination(flightDetails.getDestination());
        }
        if (flightDetails.getAvailableSeats() != null) {
            flight.setAvailableSeats(flightDetails.getAvailableSeats());
        }

        Flight updatedFlight = flightRepository.save(flight);
        return convertToDTO(updatedFlight);
    }

    public FlightDTO updateSeats(UUID flightUid, Integer seats) {
        Flight flight = flightRepository.findById(flightUid)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found: " + flightUid));
        flight.setAvailableSeats(seats);
        Flight updatedFlight = flightRepository.save(flight);
        return convertToDTO(updatedFlight);
    }

    public void deleteFlight(UUID flightUid) {
        Flight flight = flightRepository.findById(flightUid)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found: " + flightUid));
        flightRepository.delete(flight);
    }

    public FlightDTO reduceSeats(UUID flightUid, Integer count) {
        Flight flight = flightRepository.findById(flightUid)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found: " + flightUid));

        if (flight.getAvailableSeats() < count) {
            throw new InvalidSeatOperationException("Not enough available seats");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - count);
        Flight updatedFlight = flightRepository.save(flight);
        return convertToDTO(updatedFlight);
    }

    public FlightDTO addSeats(UUID flightUid, Integer count) {
        Flight flight = flightRepository.findById(flightUid)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found: " + flightUid));

        flight.setAvailableSeats(flight.getAvailableSeats() + count);
        Flight updatedFlight = flightRepository.save(flight);
        return convertToDTO(updatedFlight);
    }

    private FlightDTO convertToDTO(Flight flight) {
        return new FlightDTO(
                flight.getFlightUid(),
                flight.getFlightNumber(),
                flight.getDeparture(),
                flight.getDestination(),
                flight.getAvailableSeats()
        );
    }
}