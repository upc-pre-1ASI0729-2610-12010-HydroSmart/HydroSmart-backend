package com.hydrosmart.backend.iam.domain.model.aggregates;

import com.hydrosmart.backend.iam.domain.model.valueobjects.Email;
import com.hydrosmart.backend.iam.domain.model.valueobjects.HashedPassword;
import com.hydrosmart.backend.iam.domain.model.valueobjects.UserRoleVO;

import java.time.Instant;

public class User {
    private final Long id;
    private final String name;
    private final String lastName;
    private final Email email;
    private final HashedPassword passwordHash;
    private final String phone;
    private final UserRoleVO role;
    private final String street;
    private final String city;
    private final String district;
    private final String country;
    private final boolean isActive;
    private final Instant createdAt;

    private User(Long id, String name, String lastName, Email email, HashedPassword passwordHash,
                 String phone, UserRoleVO role, String street, String city, String district,
                 String country, boolean isActive, Instant createdAt) {
        this.id = id; this.name = name; this.lastName = lastName;
        this.email = email; this.passwordHash = passwordHash;
        this.phone = phone; this.role = role;
        this.street = street; this.city = city; this.district = district; this.country = country;
        this.isActive = isActive; this.createdAt = createdAt;
    }

    public static User create(String name, String lastName, Email email, HashedPassword passwordHash,
                              String phone, UserRoleVO role) {
        return new User(null, name, lastName, email, passwordHash, phone, role,
                null, null, null, null, true, null);
    }

    public static User rehydrate(Long id, String name, String lastName, Email email,
                                 HashedPassword passwordHash, String phone, UserRoleVO role,
                                 String street, String city, String district, String country,
                                 boolean isActive, Instant createdAt) {
        return new User(id, name, lastName, email, passwordHash, phone, role,
                street, city, district, country, isActive, createdAt);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getLastName() { return lastName; }
    public Email getEmail() { return email; }
    public HashedPassword getPasswordHash() { return passwordHash; }
    public String getPhone() { return phone; }
    public UserRoleVO getRole() { return role; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getDistrict() { return district; }
    public String getCountry() { return country; }
    public boolean isActive() { return isActive; }
    public Instant getCreatedAt() { return createdAt; }

    public String fullName() { return name + " " + lastName; }
}
