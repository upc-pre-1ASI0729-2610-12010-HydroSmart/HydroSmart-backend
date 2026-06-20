package com.hydrosmart.backend.consumption.infrastructure.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record UpdateSensorStatusRequestResource(@NotBlank String status) {}
