package com.flight.booking.repository;

import com.flight.booking.entity.Booking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void shouldFindBookingsByUserUid() {
        // 给定
        UUID userUid = UUID.randomUUID();
        UUID flightUid = UUID.randomUUID();

        Booking booking1 = new Booking(userUid, flightUid, "CONFIRMED");
        Booking booking2 = new Booking(userUid, flightUid, "CANCELLED");

        entityManager.persistAndFlush(booking1);
        entityManager.persistAndFlush(booking2);

        // 当
        List<Booking> found = bookingRepository.findByUserUid(userUid);

        // 那么
        assertThat(found).hasSize(2);
        assertThat(found).extracting(Booking::getStatus)
                .containsExactlyInAnyOrder("CONFIRMED", "CANCELLED");
    }

    @Test
    void shouldFindBookingsByFlightUid() {
        // 给定
        UUID userUid = UUID.randomUUID();
        UUID flightUid = UUID.randomUUID();

        Booking booking = new Booking(userUid, flightUid, "CONFIRMED");
        entityManager.persistAndFlush(booking);

        // 当
        List<Booking> found = bookingRepository.findByFlightUid(flightUid);

        // 那么
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getUserUid()).isEqualTo(userUid);
        assertThat(found.get(0).getFlightUid()).isEqualTo(flightUid);
    }

    @Test
    void shouldReturnEmptyListForNonExistingUserUid() {
        // 当
        List<Booking> found = bookingRepository.findByUserUid(UUID.randomUUID());

        // 那么
        assertThat(found).isEmpty();
    }
}