package com.hydrosmart.backend.consumption.domain.model.valueobjects;

import java.util.Set;

public record SensorType(String value) {
    private static final Set<String> ALLOWED = Set.of("smart-meter", "flow-sensor", "leak-detector");

    public SensorType {
        if (value == null || !ALLOWED.contains(value))
            throw new IllegalArgumentException("consumption.error.sensorType.invalid");
    }
}
