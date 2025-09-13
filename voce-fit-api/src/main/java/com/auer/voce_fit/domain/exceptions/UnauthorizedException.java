package com.auer.voce_fit.domain.exceptions;

public class UnauthorizedException extends DomainException {
    public UnauthorizedException(String message) {
        super(message);
    }
}