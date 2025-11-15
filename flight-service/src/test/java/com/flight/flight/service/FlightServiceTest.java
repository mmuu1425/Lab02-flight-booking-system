package com.flight.flight.service;

import com.flight.flight.dto.FlightDTO;
import com.flight.flight.entity.Flight;
import com.flight.flight.repository.FlightRepository;
import com.flight.flight.exception.FlightNotFoundException;
import com.flight.flight.exception.InvalidSeatOperationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    void shouldCreateFlight() {
        // 给定
        Flight flight = new Flight("CA123", "Shanghai", "Beijing", 150);
        Flight savedFlight = new Flight("CA123", "Shanghai", "Beijing", 150);
        savedFlight.setFlightUid(UUID.randomUUID());

        when(flightRepository.save(any(Flight.class))).thenReturn(savedFlight);

        // 当
        FlightDTO result = flightService.createFlight(flight);

        // 那么
        assertThat(result.getFlightNumber()).isEqualTo("CA123");
        assertThat(result.getDeparture()).isEqualTo("Shanghai");
        verify(flightRepository).save(flight);
    }

    @Test
    void shouldGetFlightByNumber() {
        // 给定
        UUID flightUid = UUID.randomUUID();
        Flight flight = new Flight("CA123", "Shanghai", "Beijing", 150);
        flight.setFlightUid(flightUid);

        when(flightRepository.findByFlightNumber("CA123")).thenReturn(Optional.of(flight));

        // 当
        FlightDTO result = flightService.getFlightByNumber("CA123");

        // 那么
        assertThat(result.getFlightUid()).isEqualTo(flightUid);
        assertThat(result.getFlightNumber()).isEqualTo("CA123");
    }

    @Test
    void shouldThrowExceptionWhenFlightNotFoundByNumber() {
        // 给定
        when(flightRepository.findByFlightNumber("NONEXISTENT")).thenReturn(Optional.empty());

        // 当 + 那么
        assertThatThrownBy(() -> flightService.getFlightByNumber("NONEXISTENT"))
                .isInstanceOf(FlightNotFoundException.class)
                .hasMessage("Flight not found: NONEXISTENT");
    }

    @Test
    void shouldReduceSeats() {
        // 给定
        UUID flightUid = UUID.randomUUID();
        Flight flight = new Flight("CA123", "Shanghai", "Beijing", 150);
        flight.setFlightUid(flightUid);

        when(flightRepository.findById(flightUid)).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        // 当
        FlightDTO result = flightService.reduceSeats(flightUid, 10);

        // 那么
        assertThat(result.getAvailableSeats()).isEqualTo(140);
        verify(flightRepository).save(flight);
    }

    @Test
    void shouldNotReduceSeatsWhenNotEnoughAvailable() {
        // 给定
        UUID flightUid = UUID.randomUUID();
        Flight flight = new Flight("CA123", "Shanghai", "Beijing", 5);
        flight.setFlightUid(flightUid);

        when(flightRepository.findById(flightUid)).thenReturn(Optional.of(flight));

        // 当 + 那么
        assertThatThrownBy(() -> flightService.reduceSeats(flightUid, 10))
                .isInstanceOf(InvalidSeatOperationException.class)
                .hasMessage("Not enough available seats");

        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    void shouldAddSeats() {
        // 给定
        UUID flightUid = UUID.randomUUID();
        Flight flight = new Flight("CA123", "Shanghai", "Beijing", 150);
        flight.setFlightUid(flightUid);

        when(flightRepository.findById(flightUid)).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        // 当
        FlightDTO result = flightService.addSeats(flightUid, 10);

        // 那么
        assertThat(result.getAvailableSeats()).isEqualTo(160);
        verify(flightRepository).save(flight);
    }
}