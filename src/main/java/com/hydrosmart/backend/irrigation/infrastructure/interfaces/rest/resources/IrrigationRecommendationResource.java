package com.hydrosmart.backend.irrigation.infrastructure.interfaces.rest.resources;

public record IrrigationRecommendationResource(
        boolean shouldIrrigate,
        String recommendation,
        double rainMmNext24h,
        double temperatureC,
        String weatherCondition,
        double latitude,
        double longitude
) {}
