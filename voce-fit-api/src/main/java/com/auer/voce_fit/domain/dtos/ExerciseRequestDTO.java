package com.auer.voce_fit.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ExerciseRequestDTO(
        UUID id,
        UUID workoutId,

        @NotBlank(message = "Exercise name is required")
        String name,

        @NotBlank(message = "Sets is required")
        String sets,

        @NotBlank(message = "Reps is required")
        String reps,

        String weight, // poderia ser BigDecimal se for sempre numérico

        @NotNull(message = "Rest time is required")
        @Positive(message = "Rest time must be positive")
        Integer restTime,

        String notes,
        Integer sequence
) {}
