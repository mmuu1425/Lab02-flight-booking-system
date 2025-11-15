package com.flight.booking.repository;

import com.flight.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByUserUid(UUID userUid);

    List<Booking> findByFlightUid(UUID flightUid);
}