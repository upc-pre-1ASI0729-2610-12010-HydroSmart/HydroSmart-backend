package com.hydrosmart.backend.iam.domain.model.valueobjects;

public record UserRoleVO(String value) {
    public UserRoleVO {
        if (value == null || (!value.equals("BUILDING_ADMIN") && !value.equals("TENANT")))
            throw new IllegalArgumentException("iam.error.role.invalid");
    }
}
