package com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.adapters;

import com.hydrosmart.backend.consumption.domain.model.aggregates.ConsumptionReading;
import com.hydrosmart.backend.consumption.domain.model.repositories.ConsumptionReadingDomainRepository;
import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.mappers.ConsumptionReadingPersistenceMapper;
import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.repositories.ConsumptionReadingJpaRepository;
import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.repositories.SensorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ConsumptionReadingPersistenceAdapter implements ConsumptionReadingDomainRepository {

    private final ConsumptionReadingJpaRepository jpaRepository;
    private final SensorJpaRepository sensorJpaRepository;

    @Override
    public List<ConsumptionReading> findBySensorIdBetween(Long sensorId, Instant from, Instant to) {
        return jpaRepository.findBySensorIdAndRecordedAtBetween(sensorId, from, to).stream()
                .map(ConsumptionReadingPersistenceMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<ConsumptionReading> findBySensorIdsBetween(List<Long> sensorIds, Instant from, Instant to) {
        if (sensorIds.isEmpty()) return List.of();
        return jpaRepository.findBySensorIdInAndRecordedAtBetween(sensorIds, from, to).stream()
                .map(ConsumptionReadingPersistenceMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public ConsumptionReading save(ConsumptionReading reading) {
        return ConsumptionReadingPersistenceMapper.toDomain(
                jpaRepository.save(ConsumptionReadingPersistenceMapper.toJpaEntity(reading, sensorJpaRepository)));
    }

    @Override
    public void saveAll(List<ConsumptionReading> readings) {
        jpaRepository.saveAll(readings.stream()
                .map(r -> ConsumptionReadingPersistenceMapper.toJpaEntity(r, sensorJpaRepository))
                .collect(Collectors.toList()));
    }
}
