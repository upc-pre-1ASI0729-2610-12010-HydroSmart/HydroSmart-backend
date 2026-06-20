package com.hydrosmart.backend.savings.infrastructure.persistence.jpa.repositories;

import com.hydrosmart.backend.savings.infrastructure.persistence.jpa.entities.SavingGoalJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavingGoalJpaRepository extends JpaRepository<SavingGoalJpaEntity, Long> {
    Optional<SavingGoalJpaEntity> findByBuildingIdAndStatus(Long buildingId, String status);
}
