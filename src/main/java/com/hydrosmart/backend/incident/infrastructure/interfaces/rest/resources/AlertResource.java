package com.hydrosmart.backend.incident.infrastructure.interfaces.rest.resources;

public record AlertResource(
        Long id,
        String type,
        String title,
        String message,
        String severity,
        String status,
        String detectedAt,
        Double estimatedLoss,
        Long sensorId,
        String sensorName,
        String unitNumber
) {}
