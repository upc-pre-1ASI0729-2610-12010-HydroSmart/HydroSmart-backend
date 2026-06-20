package com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.mappers;

import com.hydrosmart.backend.consumption.domain.model.aggregates.Sensor;
import com.hydrosmart.backend.consumption.domain.model.valueobjects.SensorPreferences;
import com.hydrosmart.backend.consumption.domain.model.valueobjects.SensorStatus;
import com.hydrosmart.backend.consumption.domain.model.valueobjects.SensorType;
import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.entities.SensorJpaEntity;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities.UnitJpaEntity;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.repositories.UnitJpaRepository;

public final class SensorPersistenceMapper {
    private SensorPersistenceMapper() {}

    public static SensorJpaEntity toJpaEntity(Sensor s, UnitJpaRepository unitRepo) {
        UnitJpaEntity unit = unitRepo.findById(s.getUnitId())
                .orElseThrow(() -> new IllegalStateException("Unit not found: " + s.getUnitId()));
        SensorPreferences p = s.getPreferences();

        return SensorJpaEntity.builder()
                .id(s.getId())
                .unit(unit)
                .name(s.getName())
                .type(s.getType().value())
                .location(s.getLocation())
                .status(s.getStatus().value())
                .currentFlowLPM(s.getCurrentFlowLPM())
                .totalConsumptionLiters(s.getTotalConsumptionLiters())
                .lastActiveAt(s.getLastActiveAt())
                .installationDate(s.getInstallationDate())
                .prefDetectLeaks(p.detectLeaks())
                .prefTrackDaily(p.trackDailyConsumption())
                .prefTrackMonthly(p.trackMonthlyConsumption())
                .prefAlertsAnomaly(p.sendAlertsOnAnomaly())
                .prefWeeklyReports(p.sendWeeklyReports())
                .prefMonthlyReports(p.sendMonthlyReports())
                .prefEnergyTracking(p.enableEnergyTracking())
                .prefHighPressure(p.alertOnHighPressure())
                .build();
    }

    public static Sensor toDomain(SensorJpaEntity e) {
        return Sensor.rehydrate(
                e.getId(),
                e.getUnit().getId(),
                e.getUnit().getUnitNumber(),
                e.getName(),
                new SensorType(e.getType()),
                e.getLocation(),
                new SensorStatus(e.getStatus()),
                e.getCurrentFlowLPM(),
                e.getTotalConsumptionLiters(),
                e.getLastActiveAt(),
                e.getInstallationDate(),
                new SensorPreferences(
                        e.isPrefDetectLeaks(), e.isPrefTrackDaily(), e.isPrefTrackMonthly(),
                        e.isPrefAlertsAnomaly(), e.isPrefWeeklyReports(), e.isPrefMonthlyReports(),
                        e.isPrefEnergyTracking(), e.isPrefHighPressure())
        );
    }
}
