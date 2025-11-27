package com.example.gatewayservice.dto;

import java.util.UUID;

public class TicketResponse {
    private UUID ticketUid;
    private String flightNumber;
    private Integer price;
    private String status;

    public TicketResponse() {}

    public TicketResponse(UUID ticketUid, String flightNumber, Integer price, String status) {
        this.ticketUid = ticketUid;
        this.flightNumber = flightNumber;
        this.price = price;
        this.status = status;
    }

    // Getters and Setters
    public UUID getTicketUid() { return ticketUid; }
    public void setTicketUid(UUID ticketUid) { this.ticketUid = ticketUid; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}