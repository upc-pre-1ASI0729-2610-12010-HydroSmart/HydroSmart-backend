package com.hydrosmart.backend.incident.infrastructure.persistence.jpa.mappers;

import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.entities.SensorJpaEntity;
import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.repositories.SensorJpaRepository;
import com.hydrosmart.backend.incident.domain.model.aggregates.Alert;
import com.hydrosmart.backend.incident.domain.model.valueobjects.AlertSeverity;
import com.hydrosmart.backend.incident.domain.model.valueobjects.AlertStatus;
import com.hydrosmart.backend.incident.infrastructure.persistence.jpa.entities.AlertJpaEntity;

public final class AlertPersistenceMapper {
    private AlertPersistenceMapper() {}

    public static AlertJpaEntity toJpaEntity(Alert a, SensorJpaRepository sensorRepo) {
        SensorJpaEntity sensor = sensorRepo.findById(a.getSensorId())
                .orElseThrow(() -> new IllegalStateException("Sensor not found: " + a.getSensorId()));
        return AlertJpaEntity.builder()
                .id(a.getId())
                .sensor(sensor)
                .type(a.getType())
                .title(a.getTitle())
                .message(a.getMessage())
                .severity(a.getSeverity().value())
                .status(a.getStatus().value())
                .detectedAt(a.getDetectedAt())
                .acknowledgedAt(a.getAcknowledgedAt())
                .resolvedAt(a.getResolvedAt())
                .estimatedLoss(a.getEstimatedLoss())
                .build();
    }

    public static Alert toDomain(AlertJpaEntity e) {
        return Alert.rehydrate(
                e.getId(),
                e.getSensor().getId(),
                e.getSensor().getName(),
                e.getSensor().getUnit().getUnitNumber(),
                e.getType(),
                e.getTitle(),
                e.getMessage(),
                new AlertSeverity(e.getSeverity()),
                new AlertStatus(e.getStatus()),
                e.getDetectedAt(),
                e.getAcknowledgedAt(),
                e.getResolvedAt(),
                e.getEstimatedLoss()
        );
    }
}
