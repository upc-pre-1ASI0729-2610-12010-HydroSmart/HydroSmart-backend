package com.hydrosmart.backend.incident.domain.model.repositories;

import com.hydrosmart.backend.incident.domain.model.aggregates.Alert;

import java.util.List;
import java.util.Optional;

public interface AlertDomainRepository {
    Optional<Alert> findById(Long id);
    List<Alert> findBySensorIds(List<Long> sensorIds);
    List<Alert> findActiveBySensorIds(List<Long> sensorIds);
    List<Alert> findBySensorIdsAndStatus(List<Long> sensorIds, String status);
    long countUnresolvedBySensorId(Long sensorId);
    Alert save(Alert alert);
}
