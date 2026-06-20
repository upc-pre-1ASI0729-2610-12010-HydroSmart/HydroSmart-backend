package com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.adapters;

import com.hydrosmart.backend.consumption.domain.model.aggregates.Sensor;
import com.hydrosmart.backend.consumption.domain.model.repositories.SensorDomainRepository;
import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.mappers.SensorPersistenceMapper;
import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.repositories.SensorJpaRepository;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.repositories.UnitJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SensorPersistenceAdapter implements SensorDomainRepository {

    private final SensorJpaRepository jpaRepository;
    private final UnitJpaRepository unitJpaRepository;

    @Override
    public Optional<Sensor> findById(Long id) {
        return jpaRepository.findById(id).map(SensorPersistenceMapper::toDomain);
    }

    @Override
    public List<Sensor> findByUnitId(Long unitId) {
        return jpaRepository.findByUnitId(unitId).stream()
                .map(SensorPersistenceMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Sensor> findByBuildingId(Long buildingId) {
        return jpaRepository.findByUnitBuildingId(buildingId).stream()
                .map(SensorPersistenceMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Sensor save(Sensor sensor) {
        return SensorPersistenceMapper.toDomain(
                jpaRepository.save(SensorPersistenceMapper.toJpaEntity(sensor, unitJpaRepository)));
    }
}
