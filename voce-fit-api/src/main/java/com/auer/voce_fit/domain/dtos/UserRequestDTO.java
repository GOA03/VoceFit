package com.auer.voce_fit.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UserRequestDTO(
        UUID id,
        String name,

        @NotBlank(message = "Email is required")
        String email,

        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String password
) { }
