package com.hydrosmart.backend.incident.application.internal.commandservices;

import com.hydrosmart.backend.consumption.domain.model.aggregates.Sensor;
import com.hydrosmart.backend.consumption.domain.model.repositories.SensorDomainRepository;
import com.hydrosmart.backend.incident.domain.model.aggregates.Alert;
import com.hydrosmart.backend.incident.domain.model.repositories.AlertDomainRepository;
import com.hydrosmart.backend.notification.application.internal.commandservices.NotificationCommandService;
import com.hydrosmart.backend.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentCommandService {

    private final AlertDomainRepository alertRepository;
    private final SensorDomainRepository sensorRepository;
    private final NotificationCommandService notificationCommandService;

    public List<Alert> getAlertsByBuilding(Long buildingId, String statusFilter) {
        List<Long> sensorIds = sensorRepository.findByBuildingId(buildingId).stream()
                .map(Sensor::getId).collect(Collectors.toList());

        if ("active".equals(statusFilter)) return alertRepository.findActiveBySensorIds(sensorIds);
        if (statusFilter != null && !statusFilter.isBlank())
            return alertRepository.findBySensorIdsAndStatus(sensorIds, statusFilter);
        return alertRepository.findBySensorIds(sensorIds);
    }

    public List<Alert> getAlertsByUnit(Long unitId) {
        List<Long> sensorIds = sensorRepository.findByUnitId(unitId).stream()
                .map(Sensor::getId).collect(Collectors.toList());
        return alertRepository.findBySensorIds(sensorIds);
    }

    public Alert resolveAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta no encontrada: " + alertId));
        Alert resolved = alertRepository.save(alert.resolve());

        // Disparo automático de notificación por email (servicio externo Resend)
        // al resolver una alerta. Se envía de forma defensiva para no afectar
        // la resolución si el servicio externo falla.
        try {
            String subject = "[HydroSmart] Alerta resuelta: " + resolved.getTitle();
            String message = String.format(
                    "La alerta \"%s\" (sensor: %s) fue marcada como resuelta.%nMensaje original: %s",
                    resolved.getTitle(), resolved.getSensorName(), resolved.getMessage());
            notificationCommandService.sendToDefaultRecipient(subject, message);
        } catch (Exception e) {
            log.warn("[Incident] No se pudo enviar la notificación al resolver la alerta {}: {}",
                    alertId, e.getMessage());
        }
        return resolved;
    }
}
