package com.hydrosmart.backend.savings.infrastructure.interfaces.rest.transform;

import com.hydrosmart.backend.savings.domain.model.aggregates.SavingGoal;
import com.hydrosmart.backend.savings.infrastructure.interfaces.rest.resources.SavingGoalResource;

public final class SavingGoalResourceAssembler {
    private SavingGoalResourceAssembler() {}

    public static SavingGoalResource toResource(SavingGoal g) {
        return new SavingGoalResource(
                g.getId(),
                g.getName(),
                g.getTargetVolumeLiters(),
                g.getCurrentVolumeLiters(),
                g.getMonthlyBudget() != null ? g.getMonthlyBudget().doubleValue() : null,
                g.getStatus(),
                g.progressPercent(),
                g.isAchieved(),
                g.recommendationsAsList(),
                g.getStartDate() != null ? g.getStartDate().toString() : null,
                g.getEndDate() != null ? g.getEndDate().toString() : null
        );
    }
}
