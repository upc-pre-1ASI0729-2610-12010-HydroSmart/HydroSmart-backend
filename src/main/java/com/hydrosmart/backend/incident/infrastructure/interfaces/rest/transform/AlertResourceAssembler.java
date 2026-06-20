package com.hydrosmart.backend.incident.infrastructure.interfaces.rest.transform;

import com.hydrosmart.backend.incident.domain.model.aggregates.Alert;
import com.hydrosmart.backend.incident.infrastructure.interfaces.rest.resources.AlertResource;

public final class AlertResourceAssembler {
    private AlertResourceAssembler() {}

    public static AlertResource toResource(Alert a) {
        return new AlertResource(
                a.getId(),
                a.getType(),
                a.getTitle(),
                a.getMessage(),
                a.getSeverity().value(),
                a.getStatus().value(),
                a.getDetectedAt() != null ? a.getDetectedAt().toString() : null,
                a.getEstimatedLoss(),
                a.getSensorId(),
                a.getSensorName(),
                a.getUnitNumber()
        );
    }
}
