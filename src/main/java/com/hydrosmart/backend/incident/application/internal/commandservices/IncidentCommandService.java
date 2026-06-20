package com.hydrosmart.backend.incident.application.internal.commandservices;

import com.hydrosmart.backend.consumption.domain.model.aggregates.Sensor;
import com.hydrosmart.backend.consumption.domain.model.repositories.SensorDomainRepository;
import com.hydrosmart.backend.incident.domain.model.aggregates.Alert;
import com.hydrosmart.backend.incident.domain.model.repositories.AlertDomainRepository;
import com.hydrosmart.backend.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentCommandService {

    private final AlertDomainRepository alertRepository;
    private final SensorDomainRepository sensorRepository;

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
        return alertRepository.save(alert.resolve());
    }
}
