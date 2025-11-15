package com.flight.flight.dto;

import java.util.UUID;

public class FlightDTO {
    private UUID flightUid;
    private String flightNumber;
    private String departure;
    private String destination;
    private Integer availableSeats;

    public FlightDTO() {}

    public FlightDTO(UUID flightUid, String flightNumber, String departure, String destination, Integer availableSeats) {
        this.flightUid = flightUid;
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