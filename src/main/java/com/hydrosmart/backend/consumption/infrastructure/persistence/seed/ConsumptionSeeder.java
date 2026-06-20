package com.hydrosmart.backend.consumption.infrastructure.persistence.seed;

import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.entities.ConsumptionReadingJpaEntity;
import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.entities.SensorJpaEntity;
import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.repositories.ConsumptionReadingJpaRepository;
import com.hydrosmart.backend.consumption.infrastructure.persistence.jpa.repositories.SensorJpaRepository;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.entities.UnitJpaEntity;
import com.hydrosmart.backend.property.infrastructure.persistence.jpa.repositories.UnitJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Seeder del bounded context Consumption.
 * Se ejecuta TERCERO (Order 3): depende de que PropertySeeder ya haya
 * creado las unidades, porque los sensores necesitan vincular unitId.
 *
 * Busca las unidades por unitNumber para no depender del orden de
 * inserción de PropertySeeder.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(3)
public class ConsumptionSeeder implements ApplicationRunner {

    private final UnitJpaRepository unitRepository;
    private final SensorJpaRepository sensorRepository;
    private final ConsumptionReadingJpaRepository readingRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (sensorRepository.count() > 0) {
            log.info("[Consumption] Sensors already seeded, skipping.");
            return;
        }
        log.info("[Consumption] Seeding sensors and readings...");

        Map<String, UnitJpaEntity> units = new java.util.HashMap<>();
        for (UnitJpaEntity u : unitRepository.findAll()) {
            units.put(u.getUnitNumber(), u);
        }
        if (units.isEmpty()) {
            throw new IllegalStateException(
                    "[Consumption] No hay unidades. ¿Corrió PropertySeeder primero?");
        }

        UnitJpaEntity unit1 = units.get("1A");
        UnitJpaEntity unit2 = units.get("1B");
        UnitJpaEntity unit3 = units.get("2A");
        UnitJpaEntity unit4 = units.get("2B");
        UnitJpaEntity unit5 = units.get("3A");
        UnitJpaEntity unit6 = units.get("3B");

        SensorJpaEntity s1 = sensorRepository.save(sensor(unit1, "Medidor 1A", "smart-meter", "Sala", "active",
                1.2, 2600.0, ts("2025-05-11T14:32:00"), ts("2024-01-20T00:00:00"),
                true, true, true, true, true, true, false, true));
        SensorJpaEntity s2 = sensorRepository.save(sensor(unit1, "Detector 1A", "leak-detector", "Baño", "active",
                0.0, 2600.0, ts("2025-05-11T14:30:00"), ts("2024-01-20T00:00:00"),
                true, true, false, true, false, false, false, false));
        SensorJpaEntity s3 = sensorRepository.save(sensor(unit2, "Medidor 1B", "smart-meter", "Sala", "warning",
                3.1, 3750.0, ts("2025-05-11T12:10:00"), ts("2024-02-05T00:00:00"),
                true, true, true, true, true, true, false, true));
        SensorJpaEntity s4 = sensorRepository.save(sensor(unit2, "Detector 1B", "leak-detector", "Cocina", "active",
                0.0, 3750.0, ts("2025-05-11T14:00:00"), ts("2024-02-05T00:00:00"),
                true, true, false, true, false, false, false, false));
        SensorJpaEntity s5 = sensorRepository.save(sensor(unit3, "Medidor 2A", "smart-meter", "Sala", "active",
                1.1, 2400.0, ts("2025-05-11T14:45:00"), ts("2024-03-01T00:00:00"),
                true, true, true, true, true, false, false, false));
        SensorJpaEntity s6 = sensorRepository.save(sensor(unit3, "Detector 2A", "leak-detector", "Baño", "active",
                0.0, 2400.0, ts("2025-05-11T14:40:00"), ts("2024-03-01T00:00:00"),
                true, false, false, true, false, false, false, false));
        SensorJpaEntity s7 = sensorRepository.save(sensor(unit4, "Medidor 2B", "smart-meter", "Sala", "warning",
                2.8, 4100.0, ts("2025-05-11T13:20:00"), ts("2024-04-01T00:00:00"),
                true, true, true, true, true, true, false, true));
        SensorJpaEntity s8 = sensorRepository.save(sensor(unit4, "Detector 2B", "leak-detector", "Baño", "active",
                0.3, 4100.0, ts("2025-05-11T13:00:00"), ts("2024-04-01T00:00:00"),
                true, true, false, true, false, false, false, false));
        SensorJpaEntity s9 = sensorRepository.save(sensor(unit5, "Medidor 3A", "smart-meter", "Sala", "active",
                1.5, 3050.0, ts("2025-05-11T14:50:00"), ts("2024-05-01T00:00:00"),
                true, true, true, true, true, true, false, false));
        SensorJpaEntity s10 = sensorRepository.save(sensor(unit5, "Detector 3A", "leak-detector", "Cocina", "active",
                0.0, 3050.0, ts("2025-05-11T14:48:00"), ts("2024-05-01T00:00:00"),
                true, false, false, true, false, false, false, false));
        sensorRepository.save(sensor(unit6, "Medidor 3B", "smart-meter", "Sala", "inactive",
                0.0, 0.0, ts("2025-04-30T18:00:00"), ts("2024-05-15T00:00:00"),
                false, false, false, false, false, false, false, false));
        sensorRepository.save(sensor(unit6, "Detector 3B", "leak-detector", "Baño", "inactive",
                0.0, 0.0, ts("2025-04-30T18:00:00"), ts("2024-05-15T00:00:00"),
                false, false, false, false, false, false, false, false));

        double[] totals = {2600, 2600, 3750, 3750, 2400, 2400, 4100, 4100, 3050, 3050};
        SensorJpaEntity[] activeSensors = {s1, s2, s3, s4, s5, s6, s7, s8, s9, s10};

        List<ConsumptionReadingJpaEntity> readings = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        int daysInMonth = today.lengthOfMonth();

        for (int si = 0; si < activeSensors.length; si++) {
            double dailyAvg = totals[si] / 30.0;
            for (int day = 1; day <= daysInMonth; day++) {
                double liters = dailyAvg + (day % 3 == 0 ? dailyAvg * 0.1 : (day % 3 == 1 ? -dailyAvg * 0.05 : 0));
                readings.add(ConsumptionReadingJpaEntity.builder()
                        .sensor(activeSensors[si])
                        .liters(Math.max(0, liters))
                        .recordedAt(LocalDateTime.of(year, month, day, 10, 0).toInstant(ZoneOffset.UTC))
                        .build());
            }
        }
        readingRepository.saveAll(readings);

        log.info("[Consumption] 12 sensors and {} readings seeded successfully.", readings.size());
    }

    private SensorJpaEntity sensor(UnitJpaEntity unit, String name, String type, String location, String status,
            double flow, double total, Instant lastActive, Instant installed,
            boolean detectLeaks, boolean trackDaily, boolean trackMonthly, boolean alertsAnomaly,
            boolean weeklyReports, boolean monthlyReports, boolean energyTracking, boolean highPressure) {
        return SensorJpaEntity.builder()
                .unit(unit).name(name).type(type).location(location).status(status)
                .currentFlowLPM(flow).totalConsumptionLiters(total)
                .lastActiveAt(lastActive).installationDate(installed)
                .prefDetectLeaks(detectLeaks).prefTrackDaily(trackDaily).prefTrackMonthly(trackMonthly)
                .prefAlertsAnomaly(alertsAnomaly).prefWeeklyReports(weeklyReports)
                .prefMonthlyReports(monthlyReports).prefEnergyTracking(energyTracking)
                .prefHighPressure(highPressure).build();
    }

    private Instant ts(String iso) {
        return LocalDateTime.parse(iso).toInstant(ZoneOffset.UTC);
    }
}
