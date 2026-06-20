package com.hydrosmart.backend.property.infrastructure.interfaces.rest.resources;

public record BuildingResource(
        Long id,
        String name,
        String address,
        String district,
        Long adminUserId
) {}
