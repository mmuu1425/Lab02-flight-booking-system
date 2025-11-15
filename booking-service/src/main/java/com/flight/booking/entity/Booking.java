package com.flight.booking.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue
    private UUID bookingUid;

    @Column(nullable = false)
    private UUID userUid;

    @Column(nullable = false)
    private UUID flightUid;

    @Column(nullable = false)
    private String status; // "RESERVED", "CONFIRMED", "CANCELLED"

    // 默认构造函数
    public Booking() {}

    // 全参构造函数
    public Booking(UUID userUid, UUID flightUid, String status) {
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