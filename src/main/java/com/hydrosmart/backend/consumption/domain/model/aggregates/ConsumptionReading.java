package com.hydrosmart.backend.consumption.domain.model.aggregates;

import java.time.Instant;

public class ConsumptionReading {
    private final Long id;
    private final Long sensorId;
    private final Double liters;
    private final Instant recordedAt;

    private ConsumptionReading(Long id, Long sensorId, Double liters, Instant recordedAt) {
        this.id = id; this.sensorId = sensorId; this.liters = liters; this.recordedAt = recordedAt;
    }

    public static ConsumptionReading rehydrate(Long id, Long sensorId, Double liters, Instant recordedAt) {
        return new ConsumptionReading(id, sensorId, liters, recordedAt);
    }

    public Long getId() { return id; }
    public Long getSensorId() { return sensorId; }
    public Double getLiters() { return liters; }
    public Instant getRecordedAt() { return recordedAt; }
}
