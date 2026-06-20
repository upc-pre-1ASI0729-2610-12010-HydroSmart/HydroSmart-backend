package com.hydrosmart.backend.iam.domain.model.valueobjects;

public record HashedPassword(String value) {
    public HashedPassword {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("iam.error.password.blank");
    }
}
