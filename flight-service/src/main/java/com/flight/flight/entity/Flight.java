package com.flight.flight.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue
    private UUID flightUid;

    @Column(unique = true, nullable = false)
    private String flightNumber;

    @Column(nullable = false)
    private String departure;

    @Column(nullable = false)
    private String destination;

    private Integer availableSeats;

    // 默认构造函数
    public Flight() {}

    // 全参构造函数
    public Flight(String flightNumber, String departure, String destination, Integer availableSeats) {
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.destination = destination;
        this.availableSeats = availableSeats;
    }

    // Getters and Setters
    public UUID getFlightUid() {
        return flightUid;
    }

    public void setFlightUid(UUID flightUid) {
        this.flightUid = flightUid;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }
}