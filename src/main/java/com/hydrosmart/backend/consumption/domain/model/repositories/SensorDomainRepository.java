package com.hydrosmart.backend.consumption.domain.model.repositories;

import com.hydrosmart.backend.consumption.domain.model.aggregates.Sensor;
import java.util.List;
import java.util.Optional;

public interface SensorDomainRepository {
    Optional<Sensor> findById(Long id);
    List<Sensor> findByUnitId(Long unitId);
    List<Sensor> findByBuildingId(Long buildingId);
    Sensor save(Sensor sensor);
}
