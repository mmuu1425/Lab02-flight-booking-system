package com.example.ticketservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketCreateRequest {
    @JsonProperty("flightNumber")
    private String flightNumber;

    @JsonProperty("price")
    private Integer price;

    // 默认构造函数
    public TicketCreateRequest() {}

    // Getters and Setters
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
}