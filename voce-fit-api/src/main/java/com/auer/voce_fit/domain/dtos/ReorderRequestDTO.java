package com.auer.voce_fit.domain.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ReorderRequestDTO(
        @NotNull(message = "Exercise ID é  obrigatório")
        UUID exerciseId,

        @NotNull(message = "Sequence é obrigatória")
        @Min(value = 1, message = "Sequence deve ser maior que 0")
        Integer newSequence
) {}