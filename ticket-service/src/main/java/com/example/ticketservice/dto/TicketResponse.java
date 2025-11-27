package com.example.ticketservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class TicketResponse {
    @JsonProperty("ticketUid")
    private UUID ticketUid;

    @JsonProperty("flightNumber")
    private String flightNumber;

    @JsonProperty("fromAirport")
    private String fromAirport;

    @JsonProperty("toAirport")
    private String toAirport;

    @JsonProperty("date")
    private String date;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("status")
    private String status;

    // Constructors, Getters and Setters
    public TicketResponse() {}

    public TicketResponse(UUID ticketUid, String flightNumber, String fromAirport, String toAirport,
                          String date, Integer price, String status) {
        this.ticketUid = ticketUid;
        this.flightNumber = flightNumber;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.date = date;
        this.price = price;
        this.status = status;
    }

    public UUID getTicketUid() { return ticketUid; }
    public void setTicketUid(UUID ticketUid) { this.ticketUid = ticketUid; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getFromAirport() { return fromAirport; }
    public void setFromAirport(String fromAirport) { this.fromAirport = fromAirport; }

    public String getToAirport() { return toAirport; }
    public void setToAirport(String toAirport) { this.toAirport = toAirport; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}