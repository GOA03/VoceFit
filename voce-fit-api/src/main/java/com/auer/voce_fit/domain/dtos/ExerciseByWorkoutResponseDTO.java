package com.auer.voce_fit.domain.dtos;

import java.util.List;
import java.util.UUID;

public record ExerciseByWorkoutResponseDTO(
        UUID id,
        String title,
        int amount,
        List<ExerciseResponseDTO> exercises
) {}