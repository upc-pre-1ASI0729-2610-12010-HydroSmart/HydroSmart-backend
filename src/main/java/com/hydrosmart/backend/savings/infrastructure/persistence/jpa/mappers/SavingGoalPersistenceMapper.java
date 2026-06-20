package com.hydrosmart.backend.savings.infrastructure.persistence.jpa.mappers;

import com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities.BuildingJpaEntity;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.repositories.BuildingJpaRepository;
import com.hydrosmart.backend.savings.domain.model.aggregates.SavingGoal;
import com.hydrosmart.backend.savings.infrastructure.persistence.jpa.entities.SavingGoalJpaEntity;

public final class SavingGoalPersistenceMapper {
    private SavingGoalPersistenceMapper() {}

    public static SavingGoalJpaEntity toJpaEntity(SavingGoal g, BuildingJpaRepository buildingRepo) {
        BuildingJpaEntity b = buildingRepo.findById(g.getBuildingId())
                .orElseThrow(() -> new IllegalStateException("Building not found: " + g.getBuildingId()));
        return SavingGoalJpaEntity.builder()
                .id(g.getId())
                .building(b)
                .name(g.getName())
                .targetVolumeLiters(g.getTargetVolumeLiters())
                .currentVolumeLiters(g.getCurrentVolumeLiters())
                .monthlyBudget(g.getMonthlyBudget())
                .status(g.getStatus())
                .startDate(g.getStartDate())
                .endDate(g.getEndDate())
                .recommendations(g.getRecommendations())
                .build();
    }

    public static SavingGoal toDomain(SavingGoalJpaEntity e) {
        return SavingGoal.rehydrate(
                e.getId(),
                e.getBuilding().getId(),
                e.getName(),
                e.getTargetVolumeLiters(),
                e.getCurrentVolumeLiters(),
                e.getMonthlyBudget(),
                e.getStatus(),
                e.getStartDate(),
                e.getEndDate(),
                e.getRecommendations()
        );
    }
}
