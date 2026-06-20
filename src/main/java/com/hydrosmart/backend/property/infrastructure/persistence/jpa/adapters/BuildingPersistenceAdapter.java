package com.hydrosmart.backend.property.infrastructure.persistence.jpa.adapters;

import com.hydrosmart.backend.property.domain.model.aggregates.Building;
import com.hydrosmart.backend.property.domain.model.repositories.BuildingDomainRepository;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.mappers.BuildingPersistenceMapper;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.repositories.BuildingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BuildingPersistenceAdapter implements BuildingDomainRepository {

    private final BuildingJpaRepository jpaRepository;

    @Override
    public Optional<Building> findById(Long id) {
        return jpaRepository.findById(id).map(BuildingPersistenceMapper::toDomain);
    }

    @Override
    public Building save(Building building) {
        return BuildingPersistenceMapper.toDomain(
                jpaRepository.save(BuildingPersistenceMapper.toJpaEntity(building)));
    }
}
