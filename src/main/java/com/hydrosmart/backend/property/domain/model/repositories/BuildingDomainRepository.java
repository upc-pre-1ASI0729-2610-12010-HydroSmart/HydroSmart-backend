package com.hydrosmart.backend.property.domain.model.repositories;

import com.hydrosmart.backend.property.domain.model.aggregates.Building;
import java.util.Optional;

public interface BuildingDomainRepository {
    Optional<Building> findById(Long id);
    Building save(Building building);
}
