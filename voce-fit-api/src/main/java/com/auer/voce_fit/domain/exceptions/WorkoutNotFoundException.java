package com.auer.voce_fit.domain.exceptions;

public class WorkoutNotFoundException extends DomainException {
    public WorkoutNotFoundException(String message) {
        super(message);
    }
}