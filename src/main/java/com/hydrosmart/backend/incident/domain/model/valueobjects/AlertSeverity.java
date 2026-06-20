package com.hydrosmart.backend.incident.domain.model.valueobjects;

import java.util.Set;

public record AlertSeverity(String value) {
    private static final Set<String> ALLOWED = Set.of("low", "medium", "high", "critical");

    public AlertSeverity {
        if (value == null || !ALLOWED.contains(value))
            throw new IllegalArgumentException("incident.error.severity.invalid");
    }
}
