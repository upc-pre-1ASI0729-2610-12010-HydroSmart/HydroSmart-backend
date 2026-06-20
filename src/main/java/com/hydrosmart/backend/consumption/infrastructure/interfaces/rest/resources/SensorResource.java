package com.hydrosmart.backend.consumption.infrastructure.interfaces.rest.resources;

public record SensorResource(
        Long id,
        String name,
        String type,
        String location,
        String status,
        Double currentFlowLPM,
        Double totalConsumptionLiters,
        String lastActiveAt,
        String unitId,
        Long unitIdNumeric,
        String unitNumber,
        SensorPreferencesResource preferences,
        Integer unresolvedAlertCount
) {}
