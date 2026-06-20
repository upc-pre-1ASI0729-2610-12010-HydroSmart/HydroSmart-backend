package com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "buildings")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildingJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String district;

    @Column(name = "admin_user_id", nullable = false)
    private Long adminUserId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
