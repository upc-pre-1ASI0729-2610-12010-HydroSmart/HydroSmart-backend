package com.hydrosmart.backend.property.infrastructure.interfaces.rest.resources;

public record UnitResource(
        Long id,
        Long buildingId,
        String unitNumber,
        Integer floor,
        Double monthlyLimitLiters,
        Double penaltyPerExcessLiter,
        Long tenantUserId,
        Double currentConsumptionLiters,
        String tenantName
) {}
