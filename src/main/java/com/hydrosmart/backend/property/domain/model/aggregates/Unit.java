package com.hydrosmart.backend.property.domain.model.aggregates;

import com.hydrosmart.backend.property.domain.model.valueobjects.UnitNumber;

import java.math.BigDecimal;

public class Unit {
    private final Long id;
    private final Long buildingId;
    private final UnitNumber unitNumber;
    private final Integer floor;
    private final Double monthlyLimitLiters;
    private final BigDecimal penaltyPerExcessLiter;
    private final Long tenantUserId;
    private final Double currentConsumptionLiters;

    private Unit(Long id, Long buildingId, UnitNumber unitNumber, Integer floor,
                 Double monthlyLimitLiters, BigDecimal penaltyPerExcessLiter,
                 Long tenantUserId, Double currentConsumptionLiters) {
        this.id = id; this.buildingId = buildingId; this.unitNumber = unitNumber;
        this.floor = floor; this.monthlyLimitLiters = monthlyLimitLiters;
        this.penaltyPerExcessLiter = penaltyPerExcessLiter;
        this.tenantUserId = tenantUserId;
        this.currentConsumptionLiters = currentConsumptionLiters;
    }

    public static Unit rehydrate(Long id, Long buildingId, UnitNumber unitNumber, Integer floor,
                                 Double monthlyLimitLiters, BigDecimal penaltyPerExcessLiter,
                                 Long tenantUserId, Double currentConsumptionLiters) {
        return new Unit(id, buildingId, unitNumber, floor, monthlyLimitLiters,
                penaltyPerExcessLiter, tenantUserId, currentConsumptionLiters);
    }

    public Unit withTenant(Long newTenantId) {
        return new Unit(id, buildingId, unitNumber, floor, monthlyLimitLiters,
                penaltyPerExcessLiter, newTenantId, currentConsumptionLiters);
    }

    public Unit withoutTenant() {
        return new Unit(id, buildingId, unitNumber, floor, monthlyLimitLiters,
                penaltyPerExcessLiter, null, currentConsumptionLiters);
    }

    public boolean isVacant() { return tenantUserId == null; }

    public boolean isOverLimit() {
        return currentConsumptionLiters != null && monthlyLimitLiters != null
                && currentConsumptionLiters > monthlyLimitLiters;
    }

    public int progressPercent() {
        if (monthlyLimitLiters == null || monthlyLimitLiters <= 0) return 0;
        double current = currentConsumptionLiters != null ? currentConsumptionLiters : 0;
        return (int) Math.min(100, Math.round((current / monthlyLimitLiters) * 100));
    }

    public Long getId() { return id; }
    public Long getBuildingId() { return buildingId; }
    public UnitNumber getUnitNumber() { return unitNumber; }
    public Integer getFloor() { return floor; }
    public Double getMonthlyLimitLiters() { return monthlyLimitLiters; }
    public BigDecimal getPenaltyPerExcessLiter() { return penaltyPerExcessLiter; }
    public Long getTenantUserId() { return tenantUserId; }
    public Double getCurrentConsumptionLiters() { return currentConsumptionLiters; }
}
