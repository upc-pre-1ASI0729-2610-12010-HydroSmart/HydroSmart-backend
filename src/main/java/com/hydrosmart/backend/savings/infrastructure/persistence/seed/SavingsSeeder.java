package com.hydrosmart.backend.savings.infrastructure.persistence.seed;

import com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities.BuildingJpaEntity;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.repositories.BuildingJpaRepository;
import com.hydrosmart.backend.savings.infrastructure.persistence.jpa.entities.SavingGoalJpaEntity;
import com.hydrosmart.backend.savings.infrastructure.persistence.jpa.repositories.SavingGoalJpaRepository;
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
import java.util.List;

/**
 * Seeder del bounded context Savings.
 * Se ejecuta QUINTO y ÚLTIMO (Order 5): depende de que PropertySeeder
 * ya haya creado el edificio, porque la meta de ahorro necesita
 * vincular buildingId.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(5)
public class SavingsSeeder implements ApplicationRunner {

    private final BuildingJpaRepository buildingRepository;
    private final SavingGoalJpaRepository savingGoalRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (savingGoalRepository.count() > 0) {
            log.info("[Savings] Saving goals already seeded, skipping.");
            return;
        }
        log.info("[Savings] Seeding saving goal...");

        List<BuildingJpaEntity> buildings = buildingRepository.findAll();
        if (buildings.isEmpty()) {
            throw new IllegalStateException(
                    "[Savings] No hay edificios. ¿Corrió PropertySeeder primero?");
        }
        BuildingJpaEntity building = buildings.get(0);

        savingGoalRepository.save(SavingGoalJpaEntity.builder()
                .building(building).name("Meta Mayo 2025")
                .targetVolumeLiters(7500.0).currentVolumeLiters(8000.0)
                .monthlyBudget(BigDecimal.valueOf(40.00)).status("active")
                .startDate(ts("2025-05-01T00:00:00")).endDate(ts("2025-05-31T23:59:59"))
                .recommendations(
                        "Reducir tiempo de ducha en 2 minutos puede ahorrar hasta 20L diarios.|" +
                        "Riego nocturno reduce la evaporación hasta en 30%.|" +
                        "Revisar la fuga detectada en el jardín para recuperar el objetivo.")
                .build());

        log.info("[Savings] Saving goal seeded successfully.");
    }

    private Instant ts(String iso) {
        return LocalDateTime.parse(iso).toInstant(ZoneOffset.UTC);
    }
}
