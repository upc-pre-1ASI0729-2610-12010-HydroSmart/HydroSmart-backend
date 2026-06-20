package com.hydrosmart.backend.savings.domain.model.repositories;

import com.hydrosmart.backend.savings.domain.model.aggregates.SavingGoal;
import java.util.Optional;

public interface SavingGoalDomainRepository {
    Optional<SavingGoal> findByBuildingIdAndStatus(Long buildingId, String status);
    SavingGoal save(SavingGoal goal);
}
