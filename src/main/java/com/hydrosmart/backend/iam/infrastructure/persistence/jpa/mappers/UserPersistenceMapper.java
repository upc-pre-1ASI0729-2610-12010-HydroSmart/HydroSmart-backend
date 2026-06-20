package com.hydrosmart.backend.iam.infrastructure.persistence.jpa.mappers;

import com.hydrosmart.backend.iam.domain.model.aggregates.User;
import com.hydrosmart.backend.iam.domain.model.valueobjects.Email;
import com.hydrosmart.backend.iam.domain.model.valueobjects.HashedPassword;
import com.hydrosmart.backend.iam.domain.model.valueobjects.UserRoleVO;
import com.hydrosmart.backend.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import com.hydrosmart.backend.iam.infrastructure.persistence.jpa.entities.UserRole;

public final class UserPersistenceMapper {
    private UserPersistenceMapper() {}

    public static UserJpaEntity toJpaEntity(User a) {
        return UserJpaEntity.builder()
                .id(a.getId())
                .name(a.getName())
                .lastName(a.getLastName())
                .email(a.getEmail().value())
                .passwordHash(a.getPasswordHash().value())
                .phone(a.getPhone())
                .role(UserRole.valueOf(a.getRole().value()))
                .street(a.getStreet())
                .city(a.getCity())
                .district(a.getDistrict())
                .country(a.getCountry())
                .isActive(a.isActive())
                .createdAt(a.getCreatedAt())
                .build();
    }

    public static User toDomain(UserJpaEntity e) {
        return User.rehydrate(
                e.getId(), e.getName(), e.getLastName(),
                new Email(e.getEmail()),
                new HashedPassword(e.getPasswordHash()),
                e.getPhone(),
                new UserRoleVO(e.getRole().name()),
                e.getStreet(), e.getCity(), e.getDistrict(), e.getCountry(),
                e.isActive(), e.getCreatedAt()
        );
    }
}
