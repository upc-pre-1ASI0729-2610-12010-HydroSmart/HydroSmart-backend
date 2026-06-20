package com.hydrosmart.backend.property.infrastructure.persistence.jpa.mappers;

import com.hydrosmart.backend.property.domain.model.aggregates.Unit;
import com.hydrosmart.backend.property.domain.model.valueobjects.UnitNumber;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities.BuildingJpaEntity;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities.UnitJpaEntity;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.repositories.BuildingJpaRepository;

public final class UnitPersistenceMapper {
    private UnitPersistenceMapper() {}

    public static UnitJpaEntity toJpaEntity(Unit u, BuildingJpaRepository buildingRepo) {
        BuildingJpaEntity building = buildingRepo.findById(u.getBuildingId())
                .orElseThrow(() -> new IllegalStateException("Building not found: " + u.getBuildingId()));

        return UnitJpaEntity.builder()
                .id(u.getId())
                .building(building)
                .unitNumber(u.getUnitNumber().value())
                .floor(u.getFloor())
                .monthlyLimitLiters(u.getMonthlyLimitLiters())
                .penaltyPerExcessLiter(u.getPenaltyPerExcessLiter())
                .tenantUserId(u.getTenantUserId())
                .currentConsumptionLiters(u.getCurrentConsumptionLiters())
                .build();
    }

    public static Unit toDomain(UnitJpaEntity e) {
        return Unit.rehydrate(
                e.getId(),
                e.getBuilding().getId(),
                new UnitNumber(e.getUnitNumber()),
                e.getFloor(),
                e.getMonthlyLimitLiters(),
                e.getPenaltyPerExcessLiter(),
                e.getTenantUserId(),
                e.getCurrentConsumptionLiters()
        );
    }
}
