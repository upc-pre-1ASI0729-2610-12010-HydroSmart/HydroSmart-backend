package com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "units")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private BuildingJpaEntity building;

    @Column(name = "unit_number", nullable = false, length = 10)
    private String unitNumber;

    @Column(nullable = false)
    private Integer floor;

    @Column(name = "monthly_limit_liters")
    private Double monthlyLimitLiters;

    @Column(name = "penalty_per_excess_liter", precision = 10, scale = 4)
    private BigDecimal penaltyPerExcessLiter;

    @Column(name = "tenant_user_id")
    private Long tenantUserId;

    @Column(name = "current_consumption_liters")
    private Double currentConsumptionLiters;
}
