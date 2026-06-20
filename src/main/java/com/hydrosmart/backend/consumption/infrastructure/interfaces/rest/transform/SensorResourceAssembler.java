package com.hydrosmart.backend.consumption.infrastructure.interfaces.rest.transform;

import com.hydrosmart.backend.consumption.domain.model.aggregates.Sensor;
import com.hydrosmart.backend.consumption.domain.model.valueobjects.SensorPreferences;
import com.hydrosmart.backend.consumption.infrastructure.interfaces.rest.resources.SensorPreferencesResource;
import com.hydrosmart.backend.consumption.infrastructure.interfaces.rest.resources.SensorResource;

public final class SensorResourceAssembler {
    private SensorResourceAssembler() {}

    public static SensorResource toResource(Sensor s, int unresolvedAlertCount) {
        SensorPreferences p = s.getPreferences();
        return new SensorResource(
                s.getId(),
                s.getName(),
                s.getType().value(),
                s.getLocation(),
                s.getStatus().value(),
                s.getCurrentFlowLPM(),
                s.getTotalConsumptionLiters(),
                s.getLastActiveAt() != null ? s.getLastActiveAt().toString() : null,
                String.format("unit-%03d", s.getUnitId()),
                s.getUnitId(),
                s.getUnitNumber(),
                new SensorPreferencesResource(
                        p.detectLeaks(), p.trackDailyConsumption(), p.trackMonthlyConsumption(),
                        p.sendAlertsOnAnomaly(), p.sendWeeklyReports(), p.sendMonthlyReports(),
                        p.enableEnergyTracking(), p.alertOnHighPressure()),
                unresolvedAlertCount
        );
    }
}
