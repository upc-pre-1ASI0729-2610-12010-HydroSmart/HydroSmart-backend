package com.hydrosmart.backend.property.infrastructure.persistence.jpa.mappers;

import com.hydrosmart.backend.property.domain.model.aggregates.Building;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities.BuildingJpaEntity;

public final class BuildingPersistenceMapper {
    private BuildingPersistenceMapper() {}

    public static BuildingJpaEntity toJpaEntity(Building b) {
        return BuildingJpaEntity.builder()
                .id(b.getId())
                .name(b.getName())
                .address(b.getAddress())
                .district(b.getDistrict())
                .adminUserId(b.getAdminUserId())
                .createdAt(b.getCreatedAt())
                .build();
    }

    public static Building toDomain(BuildingJpaEntity e) {
        return Building.rehydrate(e.getId(), e.getName(), e.getAddress(),
                e.getDistrict(), e.getAdminUserId(), e.getCreatedAt());
    }
}
