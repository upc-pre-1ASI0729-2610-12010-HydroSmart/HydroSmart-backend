package com.hydrosmart.backend.property.domain.model.valueobjects;

public record UnitNumber(String value) {
    public UnitNumber {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("property.error.unitNumber.blank");
        if (value.length() > 10)
            throw new IllegalArgumentException("property.error.unitNumber.tooLong");
    }
}
