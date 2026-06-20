package com.hydrosmart.backend.property.infrastructure.interfaces.rest.resources;

import jakarta.validation.constraints.*;

public record AssignTenantRequestResource(
        @NotBlank String name,
        @NotBlank String lastName,
        @Email @NotBlank String email,
        String phone,
        @NotBlank String password
) {}
