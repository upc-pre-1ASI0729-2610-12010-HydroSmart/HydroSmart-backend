package com.hydrosmart.backend.property.infrastructure.interfaces.rest.transform;

import com.hydrosmart.backend.property.domain.model.aggregates.Unit;
import com.hydrosmart.backend.property.infrastructure.interfaces.rest.resources.UnitResource;

public final class UnitResourceAssembler {
    private UnitResourceAssembler() {}

    public static UnitResource toResource(Unit unit, String tenantName) {
        double penalty = unit.getPenaltyPerExcessLiter() != null
                ? unit.getPenaltyPerExcessLiter().doubleValue() : 0.0;
        return new UnitResource(
                unit.getId(),
                unit.getBuildingId(),
                unit.getUnitNumber().value(),
                unit.getFloor(),
                unit.getMonthlyLimitLiters(),
                penalty,
                unit.getTenantUserId(),
                unit.getCurrentConsumptionLiters(),
                tenantName != null ? tenantName : ""
        );
    }
}
