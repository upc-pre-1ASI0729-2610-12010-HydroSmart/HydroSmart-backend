package com.hydrosmart.backend.iam.infrastructure.interfaces.rest.transform;

import com.hydrosmart.backend.iam.domain.model.aggregates.User;
import com.hydrosmart.backend.iam.infrastructure.interfaces.rest.resources.AddressResource;
import com.hydrosmart.backend.iam.infrastructure.interfaces.rest.resources.UserResource;

public final class UserResourceAssembler {
    private UserResourceAssembler() {}

    public static UserResource toResource(User user) {
        return new UserResource(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail().value(),
                user.getPhone(),
                user.getRole().value(),
                new AddressResource(user.getStreet(), user.getCity(), user.getDistrict(), user.getCountry()),
                user.getCreatedAt() != null ? user.getCreatedAt().toString() : null,
                user.isActive()
        );
    }
}
