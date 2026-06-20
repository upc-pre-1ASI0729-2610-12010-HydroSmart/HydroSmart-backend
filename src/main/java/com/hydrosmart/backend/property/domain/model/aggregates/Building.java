package com.hydrosmart.backend.property.domain.model.aggregates;

import java.time.Instant;

public class Building {
    private final Long id;
    private final String name;
    private final String address;
    private final String district;
    private final Long adminUserId;
    private final Instant createdAt;

    private Building(Long id, String name, String address, String district, Long adminUserId, Instant createdAt) {
        this.id = id; this.name = name; this.address = address;
        this.district = district; this.adminUserId = adminUserId; this.createdAt = createdAt;
    }

    public static Building create(String name, String address, String district, Long adminUserId) {
        return new Building(null, name, address, district, adminUserId, null);
    }

    public static Building rehydrate(Long id, String name, String address, String district,
                                     Long adminUserId, Instant createdAt) {
        return new Building(id, name, address, district, adminUserId, createdAt);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getDistrict() { return district; }
    public Long getAdminUserId() { return adminUserId; }
    public Instant getCreatedAt() { return createdAt; }
}
