package com.auer.voce_fit.domain.exceptions;

public class ForbiddenException extends DomainException {
    public ForbiddenException (String message) {
        super(message);
    }
}