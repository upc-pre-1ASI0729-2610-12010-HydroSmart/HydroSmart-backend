package com.hydrosmart.backend.property.infrastructure.interfaces.rest.resources;

public record TenantDetailResource(
        Long userId,
        String name,
        String lastName,
        String email,
        String phone,
        String unitNumber,
        Integer floor,
        Double currentConsumptionLiters,
        Double monthlyLimitLiters,
        Integer progressPercent,
        String createdAt
) {}
