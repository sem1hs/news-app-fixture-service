package com.semihsahinoglu.fixture_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FixtureNotFoundException extends RuntimeException {
    public FixtureNotFoundException(String message) {
        super(message);
    }
}
