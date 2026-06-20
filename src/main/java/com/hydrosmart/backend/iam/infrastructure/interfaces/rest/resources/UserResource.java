package com.hydrosmart.backend.iam.infrastructure.interfaces.rest.resources;

public record UserResource(
        Long id,
        String name,
        String lastName,
        String email,
        String phone,
        String role,
        AddressResource address,
        String createdAt,
        boolean isActive
) {}
