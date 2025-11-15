package com.flight.gateway.dto;

public class BookingRequest {
    private String username;
    private String flightNumber;

    // 默认构造函数
    public BookingRequest() {}

    // 全参构造函数
    public BookingRequest(String username, String flightNumber) {
        this.username = username;
        this.flightNumber = flightNumber;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
}