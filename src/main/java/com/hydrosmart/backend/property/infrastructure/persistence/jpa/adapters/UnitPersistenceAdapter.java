package com.hydrosmart.backend.property.infrastructure.persistence.jpa.adapters;

import com.hydrosmart.backend.property.domain.model.aggregates.Unit;
import com.hydrosmart.backend.property.domain.model.repositories.UnitDomainRepository;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.mappers.UnitPersistenceMapper;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.repositories.BuildingJpaRepository;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.repositories.UnitJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UnitPersistenceAdapter implements UnitDomainRepository {

    private final UnitJpaRepository jpaRepository;
    private final BuildingJpaRepository buildingJpaRepository;

    @Override
    public Optional<Unit> findById(Long id) {
        return jpaRepository.findById(id).map(UnitPersistenceMapper::toDomain);
    }

    @Override
    public List<Unit> findByBuildingId(Long buildingId) {
        return jpaRepository.findByBuildingId(buildingId).stream()
                .map(UnitPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Unit> findByTenantUserId(Long tenantUserId) {
        return jpaRepository.findByTenantUserId(tenantUserId).map(UnitPersistenceMapper::toDomain);
    }

    @Override
    public Unit save(Unit unit) {
        return UnitPersistenceMapper.toDomain(
                jpaRepository.save(UnitPersistenceMapper.toJpaEntity(unit, buildingJpaRepository)));
    }
}
