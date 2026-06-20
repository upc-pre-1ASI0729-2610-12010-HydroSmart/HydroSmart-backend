package com.hydrosmart.backend.iam.domain.model.valueobjects;

import java.util.regex.Pattern;

public record Email(String value) {
    private static final Pattern PATTERN = Pattern.compile("^[^@]+@[^@]+\\.[^@]+$");

    public Email {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("iam.error.email.blank");
        if (!PATTERN.matcher(value).matches())
            throw new IllegalArgumentException("iam.error.email.invalid");
    }
}
