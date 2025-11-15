package com.flight.flight.repository;

import com.flight.flight.entity.Flight;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FlightRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FlightRepository flightRepository;

    @Test
    void shouldFindFlightByFlightNumber() {
        // 给定
        Flight flight = new Flight("CA123", "Shanghai", "Beijing", 150);
        entityManager.persistAndFlush(flight);

        // 当
        Optional<Flight> found = flightRepository.findByFlightNumber("CA123");

        // 那么
        assertThat(found).isPresent();
        assertThat(found.get().getDeparture()).isEqualTo("Shanghai");
        assertThat(found.get().getDestination()).isEqualTo("Beijing");
    }

    @Test
    void shouldCheckIfFlightNumberExists() {
        // 给定
        Flight flight = new Flight("MU456", "Beijing", "Shanghai", 200);
        entityManager.persistAndFlush(flight);

        // 当
        boolean exists = flightRepository.existsByFlightNumber("MU456");

        // 那么
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseForNonExistingFlightNumber() {
        // 当
        boolean exists = flightRepository.existsByFlightNumber("NONEXISTENT");

        // 那么
        assertThat(exists).isFalse();
    }
}