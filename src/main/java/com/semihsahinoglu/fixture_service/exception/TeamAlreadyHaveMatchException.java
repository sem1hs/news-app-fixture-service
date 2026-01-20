package com.semihsahinoglu.fixture_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TeamAlreadyHaveMatchException extends RuntimeException {
    public TeamAlreadyHaveMatchException(String message) {
        super(message);
    }
}
