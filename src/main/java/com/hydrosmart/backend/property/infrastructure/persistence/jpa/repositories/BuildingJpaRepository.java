package com.hydrosmart.backend.property.infrastructure.persistence.jpa.repositories;

import com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities.BuildingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingJpaRepository extends JpaRepository<BuildingJpaEntity, Long> {
}
