package com.hydrosmart.backend.incident.infrastructure.persistence.seed;

import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.entities.SensorJpaEntity;
import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.repositories.SensorJpaRepository;
import com.hydrosmart.backend.incident.infrastructure.persistence.jpa.entities.AlertJpaEntity;
import com.hydrosmart.backend.incident.infrastructure.persistence.jpa.repositories.AlertJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Seeder del bounded context Incident.
 * Se ejecuta CUARTO (Order 4): depende de que ConsumptionSeeder ya haya
 * creado los sensores, porque las alertas necesitan vincular sensorId.
 *
 * Busca los sensores por nombre para no depender del orden de
 * inserción de ConsumptionSeeder.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(4)
public class IncidentSeeder implements ApplicationRunner {

    private final SensorJpaRepository sensorRepository;
    private final AlertJpaRepository alertRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (alertRepository.count() > 0) {
            // Para el demo: reactivar todas las alertas a 'active' en cada arranque,
            // de modo que siempre haya alertas visibles incluso tras resolverlas.
            log.info("[Incident] Alerts already exist. Reactivating all to 'active'.");
            var existing = alertRepository.findAll();
            existing.forEach(a -> {
                a.setStatus("active");
                a.setAcknowledgedAt(null);
                a.setResolvedAt(null);
            });
            alertRepository.saveAll(existing);
            return;
        }
        log.info("[Incident] Seeding alerts...");

        List<SensorJpaEntity> sensors = sensorRepository.findAll();
        if (sensors.isEmpty()) {
            throw new IllegalStateException(
                    "[Incident] No hay sensores. ¿Corrió ConsumptionSeeder primero?");
        }

        SensorJpaEntity detector1A = findByName(sensors, "Detector 1A");
        SensorJpaEntity medidor1A = findByName(sensors, "Medidor 1A");

        alertRepository.save(AlertJpaEntity.builder()
                .sensor(detector1A).type("leak").title("Posible fuga detectada")
                .message("Sensor Jardín detecta flujo residual continuo de 0.3 L/min. Revise la línea de riego.")
                .severity("high").status("active")
                .detectedAt(ts("2025-05-11T10:05:00")).estimatedLoss(14.4).build());

        alertRepository.save(AlertJpaEntity.builder()
                .sensor(medidor1A).type("anomaly").title("Consumo anómalo detectado")
                .message("El 06/05 el consumo fue de 920L, 3.3× el promedio diario de 279L.")
                .severity("critical").status("acknowledged")
                .detectedAt(ts("2025-05-06T07:00:00"))
                .acknowledgedAt(ts("2025-05-06T09:15:00")).estimatedLoss(641.0).build());

        alertRepository.save(AlertJpaEntity.builder()
                .sensor(medidor1A).type("threshold").title("Meta mensual en riesgo")
                .message("Al ritmo actual, superarás tu meta de 7500L para fin de mes.")
                .severity("medium").status("active")
                .detectedAt(ts("2025-05-10T08:00:00")).estimatedLoss(0.0).build());

        log.info("[Incident] 3 alerts seeded successfully.");
    }

    private SensorJpaEntity findByName(List<SensorJpaEntity> sensors, String name) {
        return sensors.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "[Incident] No se encontró el sensor '" + name + "'"));
    }

    private Instant ts(String iso) {
        return LocalDateTime.parse(iso).toInstant(ZoneOffset.UTC);
    }
}
