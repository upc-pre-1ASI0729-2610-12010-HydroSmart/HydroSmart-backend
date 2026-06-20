package com.hydrosmart.backend.property.infrastructure.interfaces.rest.transform;

import com.hydrosmart.backend.property.domain.model.aggregates.Building;
import com.hydrosmart.backend.property.infrastructure.interfaces.rest.resources.BuildingResource;

public final class BuildingResourceAssembler {
    private BuildingResourceAssembler() {}

    public static BuildingResource toResource(Building b) {
        return new BuildingResource(b.getId(), b.getName(), b.getAddress(), b.getDistrict(), b.getAdminUserId());
    }
}
