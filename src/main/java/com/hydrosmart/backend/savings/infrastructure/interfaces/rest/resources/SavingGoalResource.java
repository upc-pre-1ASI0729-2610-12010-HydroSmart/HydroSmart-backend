package com.hydrosmart.backend.savings.infrastructure.interfaces.rest.resources;

import java.util.List;

public record SavingGoalResource(
        Long id,
        String name,
        Double targetVolumeLiters,
        Double currentVolumeLiters,
        Double monthlyBudget,
        String status,
        Integer progressPercent,
        Boolean isAchieved,
        List<String> recommendations,
        String startDate,
        String endDate
) {}
