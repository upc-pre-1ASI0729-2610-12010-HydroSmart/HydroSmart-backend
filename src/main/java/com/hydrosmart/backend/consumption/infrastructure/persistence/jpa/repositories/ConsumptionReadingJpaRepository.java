package com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.repositories;

import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.entities.ConsumptionReadingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ConsumptionReadingJpaRepository extends JpaRepository<ConsumptionReadingJpaEntity, Long> {
    List<ConsumptionReadingJpaEntity> findBySensorIdAndRecordedAtBetween(Long sensorId, Instant from, Instant to);
    List<ConsumptionReadingJpaEntity> findBySensorIdInAndRecordedAtBetween(List<Long> sensorIds, Instant from, Instant to);
}
