package com.flight.flight.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSeatOperationException extends RuntimeException {
    public InvalidSeatOperationException(String message) {
        super(message);
    }
}