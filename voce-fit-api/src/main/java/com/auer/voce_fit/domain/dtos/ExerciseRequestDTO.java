package com.auer.voce_fit.domain.dtos;

import java.util.UUID;

public record ExerciseRequestDTO (
        UUID id,
        UUID workoutId,
        String name,
        String sets,
        String reps,
        String weight,
        Integer restTime,
        String notes,
        Integer sequence
) {}
