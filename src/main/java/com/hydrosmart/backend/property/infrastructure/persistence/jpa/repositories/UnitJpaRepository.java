package com.hydrosmart.backend.property.infrastructure.persistence.jpa.repositories;

import com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities.UnitJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import java.util.List;

public interface UnitJpaRepository extends JpaRepository<UnitJpaEntity, Long> {
    List<UnitJpaEntity> findByBuildingId(Long buildingId);
    Optional<UnitJpaEntity> findByTenantUserId(Long tenantUserId);
}
