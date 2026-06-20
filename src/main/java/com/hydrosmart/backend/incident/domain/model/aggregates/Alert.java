package com.hydrosmart.backend.incident.domain.model.aggregates;

import com.hydrosmart.backend.incident.domain.model.valueobjects.AlertSeverity;
import com.hydrosmart.backend.incident.domain.model.valueobjects.AlertStatus;

import java.time.Instant;

public class Alert {
    private final Long id;
    private final Long sensorId;
    private final String sensorName;
    private final String unitNumber;
    private final String type;
    private final String title;
    private final String message;
    private final AlertSeverity severity;
    private final AlertStatus status;
    private final Instant detectedAt;
    private final Instant acknowledgedAt;
    private final Instant resolvedAt;
    private final Double estimatedLoss;

    private Alert(Long id, Long sensorId, String sensorName, String unitNumber, String type,
                  String title, String message, AlertSeverity severity, AlertStatus status,
                  Instant detectedAt, Instant acknowledgedAt, Instant resolvedAt, Double estimatedLoss) {
        this.id = id; this.sensorId = sensorId; this.sensorName = sensorName;
        this.unitNumber = unitNumber; this.type = type; this.title = title;
        this.message = message; this.severity = severity; this.status = status;
        this.detectedAt = detectedAt; this.acknowledgedAt = acknowledgedAt;
        this.resolvedAt = resolvedAt; this.estimatedLoss = estimatedLoss;
    }

    public static Alert rehydrate(Long id, Long sensorId, String sensorName, String unitNumber,
                                  String type, String title, String message, AlertSeverity severity,
                                  AlertStatus status, Instant detectedAt, Instant acknowledgedAt,
                                  Instant resolvedAt, Double estimatedLoss) {
        return new Alert(id, sensorId, sensorName, unitNumber, type, title, message,
                severity, status, detectedAt, acknowledgedAt, resolvedAt, estimatedLoss);
    }

    public Alert resolve() {
        return new Alert(id, sensorId, sensorName, unitNumber, type, title, message,
                severity, new AlertStatus("resolved"), detectedAt, acknowledgedAt, Instant.now(), estimatedLoss);
    }

    public Long getId() { return id; }
    public Long getSensorId() { return sensorId; }
    public String getSensorName() { return sensorName; }
    public String getUnitNumber() { return unitNumber; }
    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public AlertSeverity getSeverity() { return severity; }
    public AlertStatus getStatus() { return status; }
    public Instant getDetectedAt() { return detectedAt; }
    public Instant getAcknowledgedAt() { return acknowledgedAt; }
    public Instant getResolvedAt() { return resolvedAt; }
    public Double getEstimatedLoss() { return estimatedLoss; }
}
