package com.hydrosmart.backend.property.domain.model.valueobjects;

public record WaterVolume(Double liters) {
    public WaterVolume {
        if (liters == null || liters < 0)
            throw new IllegalArgumentException("property.error.waterVolume.negative");
    }
}
