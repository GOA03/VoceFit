package com.auer.voce_fit.domain.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkoutResponseDTO (
        UUID id,
        String title,
        int amount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
