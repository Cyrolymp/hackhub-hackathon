package com.hackhub.exception;

import org.springframework.http.HttpStatus;

public class HackHubException extends RuntimeException {
    private final HttpStatus status;

    public HackHubException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HackHubException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public HttpStatus getStatus() { return status; }
}
