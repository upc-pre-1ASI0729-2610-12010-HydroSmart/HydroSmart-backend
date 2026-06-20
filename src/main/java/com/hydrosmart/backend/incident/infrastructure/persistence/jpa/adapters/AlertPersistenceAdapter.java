package com.hydrosmart.backend.incident.infrastructure.persistence.jpa.adapters;

import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.repositories.SensorJpaRepository;
import com.hydrosmart.backend.incident.domain.model.aggregates.Alert;
import com.hydrosmart.backend.incident.domain.model.repositories.AlertDomainRepository;
import com.hydrosmart.backend.incident.infrastructure.persistence.jpa.mappers.AlertPersistenceMapper;
import com.hydrosmart.backend.incident.infrastructure.persistence.jpa.repositories.AlertJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AlertPersistenceAdapter implements AlertDomainRepository {

    private final AlertJpaRepository jpaRepository;
    private final SensorJpaRepository sensorJpaRepository;

    @Override
    public Optional<Alert> findById(Long id) {
        return jpaRepository.findById(id).map(AlertPersistenceMapper::toDomain);
    }

    @Override
    public List<Alert> findBySensorIds(List<Long> sensorIds) {
        if (sensorIds.isEmpty()) return List.of();
        return jpaRepository.findBySensorIdIn(sensorIds).stream()
                .map(AlertPersistenceMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Alert> findActiveBySensorIds(List<Long> sensorIds) {
        if (sensorIds.isEmpty()) return List.of();
        return jpaRepository.findActiveAlertsBySensorIds(sensorIds).stream()
                .map(AlertPersistenceMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Alert> findBySensorIdsAndStatus(List<Long> sensorIds, String status) {
        if (sensorIds.isEmpty()) return List.of();
        return jpaRepository.findBySensorIdInAndStatus(sensorIds, status).stream()
                .map(AlertPersistenceMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public long countUnresolvedBySensorId(Long sensorId) {
        return jpaRepository.countBySensorIdAndStatusNot(sensorId, "resolved");
    }

    @Override
    public Alert save(Alert alert) {
        return AlertPersistenceMapper.toDomain(
                jpaRepository.save(AlertPersistenceMapper.toJpaEntity(alert, sensorJpaRepository)));
    }
}
