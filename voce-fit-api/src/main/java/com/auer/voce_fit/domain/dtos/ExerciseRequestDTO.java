package com.auer.voce_fit.domain.dtos;

import java.util.UUID;

public record ExerciseRequestDTO (
        UUID id,
        String name,
        String sets,
        String reps,
        String weight,
        String notes,
        Integer sequence
) {}
