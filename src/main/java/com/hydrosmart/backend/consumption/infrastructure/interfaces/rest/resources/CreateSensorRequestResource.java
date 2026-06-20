package com.hydrosmart.backend.consumption.infrastructure.interfaces.rest.resources;

import jakarta.validation.constraints.*;

public record CreateSensorRequestResource(
        @NotBlank String name,
        @NotBlank String type,
        String location,
        @NotNull Long unitId
) {}
