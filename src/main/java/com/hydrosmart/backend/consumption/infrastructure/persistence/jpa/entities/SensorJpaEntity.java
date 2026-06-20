package com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.entities;

import com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities.UnitJpaEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "sensors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitJpaEntity unit;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 30)
    private String type;

    private String location;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "current_flow_lpm")
    private Double currentFlowLPM;

    @Column(name = "total_consumption_liters")
    private Double totalConsumptionLiters;

    @Column(name = "last_active_at")
    private Instant lastActiveAt;

    @Column(name = "installation_date")
    private Instant installationDate;

    @Column(name = "pref_detect_leaks") private boolean prefDetectLeaks;
    @Column(name = "pref_track_daily") private boolean prefTrackDaily;
    @Column(name = "pref_track_monthly") private boolean prefTrackMonthly;
    @Column(name = "pref_alerts_anomaly") private boolean prefAlertsAnomaly;
    @Column(name = "pref_weekly_reports") private boolean prefWeeklyReports;
    @Column(name = "pref_monthly_reports") private boolean prefMonthlyReports;
    @Column(name = "pref_energy_tracking") private boolean prefEnergyTracking;
    @Column(name = "pref_high_pressure") private boolean prefHighPressure;
}
