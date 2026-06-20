package com.hydrosmart.backend.property.infrastructure.persistence.seed;

import com.hydrosmart.backend.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import com.hydrosmart.backend.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities.BuildingJpaEntity;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities.UnitJpaEntity;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.repositories.BuildingJpaRepository;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.repositories.UnitJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Seeder del bounded context Property.
 * Se ejecuta SEGUNDO (Order 2): depende de que IamSeeder ya haya
 * creado los usuarios (admin + tenants), porque las unidades
 * necesitan vincular tenantUserId.
 *
 * IMPORTANTE: este seeder busca los usuarios por email con
 * userRepository.findByEmail(...) en lugar de recibir IDs hardcodeados,
 * para no acoplarse al orden de inserción de IamSeeder.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class PropertySeeder implements ApplicationRunner {

    private final UserJpaRepository userRepository;
    private final BuildingJpaRepository buildingRepository;
    private final UnitJpaRepository unitRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (buildingRepository.count() > 0) {
            log.info("[Property] Buildings already seeded, skipping.");
            return;
        }
        log.info("[Property] Seeding building and units...");

        UserJpaEntity admin = userRepository.findByEmail("admin@begonias.com.pe")
                .orElseThrow(() -> new IllegalStateException(
                        "[Property] No se encontró el admin. ¿Corrió IamSeeder primero?"));

        BuildingJpaEntity building = buildingRepository.save(BuildingJpaEntity.builder()
                .name("Edificio Begonias")
                .address("Av. Javier Prado 1234")
                .district("San Isidro")
                .adminUserId(admin.getId())
                .createdAt(ts("2024-01-15T00:00:00"))
                .build());

        Long u2 = userRepository.findByEmail("arianna.flores@begonias.com.pe").orElseThrow().getId();
        Long u3 = userRepository.findByEmail("juan.perez@begonias.com.pe").orElseThrow().getId();
        Long u4 = userRepository.findByEmail("maria.garcia@begonias.com.pe").orElseThrow().getId();
        Long u5 = userRepository.findByEmail("luis.torres@begonias.com.pe").orElseThrow().getId();
        Long u6 = userRepository.findByEmail("sofia.quispe@begonias.com.pe").orElseThrow().getId();

        unitRepository.save(UnitJpaEntity.builder().building(building)
                .unitNumber("1A").floor(1).monthlyLimitLiters(6000.0)
                .penaltyPerExcessLiter(BigDecimal.valueOf(0.008)).tenantUserId(u2)
                .currentConsumptionLiters(5200.0).build());

        unitRepository.save(UnitJpaEntity.builder().building(building)
                .unitNumber("1B").floor(1).monthlyLimitLiters(6000.0)
                .penaltyPerExcessLiter(BigDecimal.valueOf(0.008)).tenantUserId(u3)
                .currentConsumptionLiters(7500.0).build());

        unitRepository.save(UnitJpaEntity.builder().building(building)
                .unitNumber("2A").floor(2).monthlyLimitLiters(7000.0)
                .penaltyPerExcessLiter(BigDecimal.valueOf(0.008)).tenantUserId(u4)
                .currentConsumptionLiters(4800.0).build());

        unitRepository.save(UnitJpaEntity.builder().building(building)
                .unitNumber("2B").floor(2).monthlyLimitLiters(7000.0)
                .penaltyPerExcessLiter(BigDecimal.valueOf(0.008)).tenantUserId(u5)
                .currentConsumptionLiters(8200.0).build());

        unitRepository.save(UnitJpaEntity.builder().building(building)
                .unitNumber("3A").floor(3).monthlyLimitLiters(8000.0)
                .penaltyPerExcessLiter(BigDecimal.valueOf(0.008)).tenantUserId(u6)
                .currentConsumptionLiters(6100.0).build());

        unitRepository.save(UnitJpaEntity.builder().building(building)
                .unitNumber("3B").floor(3).monthlyLimitLiters(8000.0)
                .penaltyPerExcessLiter(BigDecimal.valueOf(0.008)).tenantUserId(null)
                .currentConsumptionLiters(0.0).build());

        log.info("[Property] Building and 6 units seeded successfully.");
    }

    private Instant ts(String iso) {
        return LocalDateTime.parse(iso).toInstant(ZoneOffset.UTC);
    }
}
