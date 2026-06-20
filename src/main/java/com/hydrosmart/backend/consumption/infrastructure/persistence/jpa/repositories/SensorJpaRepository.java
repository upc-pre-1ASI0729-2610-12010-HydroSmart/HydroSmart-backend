package com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.repositories;

import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.entities.SensorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SensorJpaRepository extends JpaRepository<SensorJpaEntity, Long> {
    List<SensorJpaEntity> findByUnitId(Long unitId);
    List<SensorJpaEntity> findByUnitBuildingId(Long buildingId);
}
