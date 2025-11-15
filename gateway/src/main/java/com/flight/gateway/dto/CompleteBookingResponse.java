package com.flight.gateway.dto;

import java.util.UUID;

public class CompleteBookingResponse {
    private boolean success;
    private String message;
    private UUID bookingUid;
    private UUID userUid;
    private UUID flightUid;
    private String status;

    // 成功响应的静态方法
    public static CompleteBookingResponse success(UUID bookingUid, UUID userUid, UUID flightUid) {
        CompleteBookingResponse response = new CompleteBookingResponse();
        response.setSuccess(true);
        response.setMessage("Booking created successfully");
        response.setBookingUid(bookingUid);
        response.setUserUid(userUid);
        response.setFlightUid(flightUid);
        response.setStatus("CONFIRMED");
        return response;
    }

    // 失败响应的静态方法
    public static CompleteBookingResponse failure(String message) {
        CompleteBookingResponse response = new CompleteBookingResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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