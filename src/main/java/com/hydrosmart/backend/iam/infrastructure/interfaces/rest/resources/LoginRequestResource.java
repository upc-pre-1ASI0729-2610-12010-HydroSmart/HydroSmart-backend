package com.hydrosmart.backend.iam.infrastructure.interfaces.rest.resources;

import jakarta.validation.constraints.*;

public record LoginRequestResource(
        @Email @NotBlank String email,
        @NotBlank String password
) {}
