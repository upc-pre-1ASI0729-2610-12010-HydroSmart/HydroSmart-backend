package com.hydrosmart.backend.consumption.domain.model.aggregates;

import com.hydrosmart.backend.consumption.domain.model.valueobjects.SensorPreferences;
import com.hydrosmart.backend.consumption.domain.model.valueobjects.SensorStatus;
import com.hydrosmart.backend.consumption.domain.model.valueobjects.SensorType;

import java.time.Instant;

public class Sensor {
    private final Long id;
    private final Long unitId;
    private final String unitNumber;
    private final String name;
    private final SensorType type;
    private final String location;
    private final SensorStatus status;
    private final Double currentFlowLPM;
    private final Double totalConsumptionLiters;
    private final Instant lastActiveAt;
    private final Instant installationDate;
    private final SensorPreferences preferences;

    private Sensor(Long id, Long unitId, String unitNumber, String name, SensorType type,
                   String location, SensorStatus status, Double currentFlowLPM,
                   Double totalConsumptionLiters, Instant lastActiveAt, Instant installationDate,
                   SensorPreferences preferences) {
        this.id = id; this.unitId = unitId; this.unitNumber = unitNumber;
        this.name = name; this.type = type; this.location = location;
        this.status = status; this.currentFlowLPM = currentFlowLPM;
        this.totalConsumptionLiters = totalConsumptionLiters;
        this.lastActiveAt = lastActiveAt; this.installationDate = installationDate;
        this.preferences = preferences;
    }

    public static Sensor create(Long unitId, String name, SensorType type, String location) {
        return new Sensor(null, unitId, null, name, type, location,
                new SensorStatus("active"), 0.0, 0.0, Instant.now(), Instant.now(),
                SensorPreferences.defaults());
    }

    public static Sensor rehydrate(Long id, Long unitId, String unitNumber, String name,
                                   SensorType type, String location, SensorStatus status,
                                   Double currentFlowLPM, Double totalConsumptionLiters,
                                   Instant lastActiveAt, Instant installationDate,
                                   SensorPreferences preferences) {
        return new Sensor(id, unitId, unitNumber, name, type, location, status, currentFlowLPM,
                totalConsumptionLiters, lastActiveAt, installationDate, preferences);
    }

    public Sensor withStatus(SensorStatus newStatus) {
        return new Sensor(id, unitId, unitNumber, name, type, location, newStatus,
                currentFlowLPM, totalConsumptionLiters, lastActiveAt, installationDate, preferences);
    }

    public Sensor withPreferences(SensorPreferences newPrefs) {
        return new Sensor(id, unitId, unitNumber, name, type, location, status,
                currentFlowLPM, totalConsumptionLiters, lastActiveAt, installationDate, newPrefs);
    }

    public Sensor activate() { return withStatus(new SensorStatus("active")); }
    public Sensor deactivate() { return withStatus(new SensorStatus("inactive")); }

    public Long getId() { return id; }
    public Long getUnitId() { return unitId; }
    public String getUnitNumber() { return unitNumber; }
    public String getName() { return name; }
    public SensorType getType() { return type; }
    public String getLocation() { return location; }
    public SensorStatus getStatus() { return status; }
    public Double getCurrentFlowLPM() { return currentFlowLPM; }
    public Double getTotalConsumptionLiters() { return totalConsumptionLiters; }
    public Instant getLastActiveAt() { return lastActiveAt; }
    public Instant getInstallationDate() { return installationDate; }
    public SensorPreferences getPreferences() { return preferences; }
}
