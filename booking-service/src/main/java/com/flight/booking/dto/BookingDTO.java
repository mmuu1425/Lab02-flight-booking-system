package com.flight.booking.dto;

import java.util.UUID;

public class BookingDTO {
    private UUID bookingUid;
    private UUID userUid;
    private UUID flightUid;
    private String status;

    public BookingDTO() {}

    public BookingDTO(UUID bookingUid, UUID userUid, UUID flightUid, String status) {
        this.bookingUid = bookingUid;
        this.userUid = userUid;
        this.flightUid = flightUid;
        this.status = status;
    }

    // Getters and Setters
    public UUID getBookingUid() {
        return bookingUid;
    }

    public void setBookingUid(UUID bookingUid) {
        this.bookingUid = bookingUid;
    }

    public UUID getUserUid() {
        return userUid;
    }

    public void setUserUid(UUID userUid) {
        this.userUid = userUid;
    }

    public UUID getFlightUid() {
        return flightUid;
    }

    public void setFlightUid(UUID flightUid) {
        this.flightUid = flightUid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}