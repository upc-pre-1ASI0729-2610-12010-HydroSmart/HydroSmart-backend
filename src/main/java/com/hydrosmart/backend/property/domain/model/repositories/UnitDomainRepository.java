package com.hydrosmart.backend.property.domain.model.repositories;

import com.hydrosmart.backend.property.domain.model.aggregates.Unit;
import java.util.List;
import java.util.Optional;

public interface UnitDomainRepository {
    Optional<Unit> findById(Long id);
    List<Unit> findByBuildingId(Long buildingId);
    Unit save(Unit unit);
}
