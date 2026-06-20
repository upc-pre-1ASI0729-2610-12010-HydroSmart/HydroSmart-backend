package com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.mappers;

import com.hydrosmart.backend.consumption.domain.model.aggregates.ConsumptionReading;
import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.entities.ConsumptionReadingJpaEntity;
import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.entities.SensorJpaEntity;
import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.repositories.SensorJpaRepository;

public final class ConsumptionReadingPersistenceMapper {
    private ConsumptionReadingPersistenceMapper() {}

    public static ConsumptionReadingJpaEntity toJpaEntity(ConsumptionReading r, SensorJpaRepository sensorRepo) {
        SensorJpaEntity sensor = sensorRepo.findById(r.getSensorId())
                .orElseThrow(() -> new IllegalStateException("Sensor not found: " + r.getSensorId()));
        return ConsumptionReadingJpaEntity.builder()
                .id(r.getId())
                .sensor(sensor)
                .liters(r.getLiters())
                .recordedAt(r.getRecordedAt())
                .build();
    }

    public static ConsumptionReading toDomain(ConsumptionReadingJpaEntity e) {
        return ConsumptionReading.rehydrate(e.getId(), e.getSensor().getId(), e.getLiters(), e.getRecordedAt());
    }
}
