package com.example.flightservice.repository;

import com.example.flightservice.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {
    Page<Flight> findAll(Pageable pageable);
    Optional<Flight> findByFlightNumber(String flightNumber);
}