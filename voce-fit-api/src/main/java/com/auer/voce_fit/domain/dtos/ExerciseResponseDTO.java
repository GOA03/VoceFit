package com.auer.voce_fit.domain.dtos;

import java.util.UUID;

public record ExerciseResponseDTO(
        UUID id,
        String name,
        String sets,
        String reps,
        String weight,
        Integer restTime,
        String notes,
        UUID workoutId,
        Integer sequence
) {}
