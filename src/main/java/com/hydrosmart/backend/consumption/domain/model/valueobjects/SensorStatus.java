package com.hydrosmart.backend.consumption.domain.model.valueobjects;

import java.util.Set;

public record SensorStatus(String value) {
    private static final Set<String> ALLOWED = Set.of("active", "inactive", "warning", "error");

    public SensorStatus {
        if (value == null || !ALLOWED.contains(value))
            throw new IllegalArgumentException("consumption.error.sensorStatus.invalid");
    }
}
