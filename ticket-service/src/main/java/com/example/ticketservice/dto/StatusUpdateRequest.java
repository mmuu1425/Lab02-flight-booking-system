package com.example.ticketservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusUpdateRequest {
    @JsonProperty("status")
    private String status;

    // 默认构造函数
    public StatusUpdateRequest() {}

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}