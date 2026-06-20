package com.hydrosmart.backend.savings.infrastructure.persistence.jpa.adapters;

import com.hydrosmart.backend.property.infrastructure.persistence.jpa.repositories.BuildingJpaRepository;
import com.hydrosmart.backend.savings.domain.model.aggregates.SavingGoal;
import com.hydrosmart.backend.savings.domain.model.repositories.SavingGoalDomainRepository;
import com.hydrosmart.backend.savings.infrastructure.persistence.jpa.mappers.SavingGoalPersistenceMapper;
import com.hydrosmart.backend.savings.infrastructure.persistence.jpa.repositories.SavingGoalJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SavingGoalPersistenceAdapter implements SavingGoalDomainRepository {

    private final SavingGoalJpaRepository jpaRepository;
    private final BuildingJpaRepository buildingJpaRepository;

    @Override
    public Optional<SavingGoal> findByBuildingIdAndStatus(Long buildingId, String status) {
        return jpaRepository.findByBuildingIdAndStatus(buildingId, status)
                .map(SavingGoalPersistenceMapper::toDomain);
    }

    @Override
    public SavingGoal save(SavingGoal goal) {
        return SavingGoalPersistenceMapper.toDomain(
                jpaRepository.save(SavingGoalPersistenceMapper.toJpaEntity(goal, buildingJpaRepository)));
    }
}
