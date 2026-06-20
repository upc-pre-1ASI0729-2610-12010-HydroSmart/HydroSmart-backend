package com.hydrosmart.backend.savings.domain.model.aggregates;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SavingGoal {
    private final Long id;
    private final Long buildingId;
    private final String name;
    private final Double targetVolumeLiters;
    private final Double currentVolumeLiters;
    private final BigDecimal monthlyBudget;
    private final String status;
    private final Instant startDate;
    private final Instant endDate;
    private final String recommendations;

    private SavingGoal(Long id, Long buildingId, String name, Double targetVolumeLiters,
                       Double currentVolumeLiters, BigDecimal monthlyBudget, String status,
                       Instant startDate, Instant endDate, String recommendations) {
        this.id = id; this.buildingId = buildingId; this.name = name;
        this.targetVolumeLiters = targetVolumeLiters; this.currentVolumeLiters = currentVolumeLiters;
        this.monthlyBudget = monthlyBudget; this.status = status;
        this.startDate = startDate; this.endDate = endDate; this.recommendations = recommendations;
    }

    public static SavingGoal rehydrate(Long id, Long buildingId, String name, Double targetVolumeLiters,
                                       Double currentVolumeLiters, BigDecimal monthlyBudget, String status,
                                       Instant startDate, Instant endDate, String recommendations) {
        return new SavingGoal(id, buildingId, name, targetVolumeLiters, currentVolumeLiters,
                monthlyBudget, status, startDate, endDate, recommendations);
    }

    public int progressPercent() {
        double target = targetVolumeLiters != null ? targetVolumeLiters : 1;
        double current = currentVolumeLiters != null ? currentVolumeLiters : 0;
        if (target <= 0) return 0;
        return (int) Math.min(100, Math.round((current / target) * 100));
    }

    public boolean isAchieved() {
        double target = targetVolumeLiters != null ? targetVolumeLiters : 0;
        double current = currentVolumeLiters != null ? currentVolumeLiters : 0;
        return current <= target;
    }

    public List<String> recommendationsAsList() {
        if (recommendations == null) return Collections.emptyList();
        return Arrays.asList(recommendations.split("\\|"));
    }

    public Long getId() { return id; }
    public Long getBuildingId() { return buildingId; }
    public String getName() { return name; }
    public Double getTargetVolumeLiters() { return targetVolumeLiters; }
    public Double getCurrentVolumeLiters() { return currentVolumeLiters; }
    public BigDecimal getMonthlyBudget() { return monthlyBudget; }
    public String getStatus() { return status; }
    public Instant getStartDate() { return startDate; }
    public Instant getEndDate() { return endDate; }
    public String getRecommendations() { return recommendations; }
}
