package com.hydrosmart.backend.savings.infrastructure.persistence.jpa.entities;

import com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities.BuildingJpaEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "saving_goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingGoalJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private BuildingJpaEntity building;

    @Column(nullable = false)
    private String name;

    @Column(name = "target_volume_liters")
    private Double targetVolumeLiters;

    @Column(name = "current_volume_liters")
    private Double currentVolumeLiters;

    @Column(name = "monthly_budget", precision = 10, scale = 2)
    private BigDecimal monthlyBudget;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(length = 1000)
    private String recommendations;
}
