package com.hydrosmart.backend.property.infrastructure.interfaces.rest.transform;

import com.hydrosmart.backend.iam.domain.model.aggregates.User;
import com.hydrosmart.backend.property.domain.model.aggregates.Unit;
import com.hydrosmart.backend.property.infrastructure.interfaces.rest.resources.TenantDetailResource;

public final class TenantDetailResourceAssembler {
    private TenantDetailResourceAssembler() {}

    public static TenantDetailResource toResource(User tenant, Unit unit) {
        return new TenantDetailResource(
                tenant.getId(),
                tenant.getName(),
                tenant.getLastName(),
                tenant.getEmail().value(),
                tenant.getPhone(),
                unit.getUnitNumber().value(),
                unit.getFloor(),
                unit.getCurrentConsumptionLiters(),
                unit.getMonthlyLimitLiters(),
                unit.progressPercent(),
                tenant.getCreatedAt() != null ? tenant.getCreatedAt().toString() : null
        );
    }
}
