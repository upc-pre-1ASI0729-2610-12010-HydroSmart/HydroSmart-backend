package com.hydrosmart.backend.consumption.application.internal.commandservices;

import com.hydrosmart.backend.consumption.domain.model.aggregates.ConsumptionReading;
import com.hydrosmart.backend.consumption.domain.model.aggregates.Sensor;
import com.hydrosmart.backend.consumption.domain.model.repositories.ConsumptionReadingDomainRepository;
import com.hydrosmart.backend.consumption.domain.model.repositories.SensorDomainRepository;
import com.hydrosmart.backend.consumption.domain.model.valueobjects.SensorPreferences;
import com.hydrosmart.backend.consumption.domain.model.valueobjects.SensorStatus;
import com.hydrosmart.backend.consumption.domain.model.valueobjects.SensorType;
import com.hydrosmart.backend.consumption.infrastructure.interfaces.rest.resources.*;
import com.hydrosmart.backend.incident.domain.model.repositories.AlertDomainRepository;
import com.hydrosmart.backend.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsumptionCommandService {

    private final SensorDomainRepository sensorRepository;
    private final ConsumptionReadingDomainRepository readingRepository;
    private final AlertDomainRepository alertRepository;

    public List<Sensor> getSensorsByBuilding(Long buildingId) {
        return sensorRepository.findByBuildingId(buildingId);
    }

    public List<Sensor> getSensorsByUnit(Long unitId) {
        return sensorRepository.findByUnitId(unitId);
    }

    public int unresolvedAlertCount(Long sensorId) {
        return (int) alertRepository.countUnresolvedBySensorId(sensorId);
    }

    public Sensor createSensor(CreateSensorRequestResource request) {
        Sensor sensor = Sensor.create(
                request.unitId(),
                request.name(),
                new SensorType(request.type()),
                request.location()
        );
        return sensorRepository.save(sensor);
    }

    public Sensor updateStatus(Long sensorId, String status) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor no encontrado: " + sensorId));
        return sensorRepository.save(sensor.withStatus(new SensorStatus(status)));
    }

    public Sensor updatePreferences(Long sensorId, SensorPreferencesResource req) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor no encontrado: " + sensorId));
        SensorPreferences prefs = new SensorPreferences(
                req.detectLeaks(), req.trackDailyConsumption(), req.trackMonthlyConsumption(),
                req.sendAlertsOnAnomaly(), req.sendWeeklyReports(), req.sendMonthlyReports(),
                req.enableEnergyTracking(), req.alertOnHighPressure());
        return sensorRepository.save(sensor.withPreferences(prefs));
    }

    public List<ReadingResource> getSensorReadings(Long sensorId, LocalDate from, LocalDate to) {
        Instant fromI = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant toI = to.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        List<ConsumptionReading> readings = readingRepository.findBySensorIdBetween(sensorId, fromI, toI);
        return aggregateByDay(readings, from, to);
    }

    public List<ReadingResource> getBuildingReadings(Long buildingId, LocalDate from, LocalDate to) {
        List<Long> sensorIds = sensorRepository.findByBuildingId(buildingId).stream()
                .map(Sensor::getId).collect(Collectors.toList());
        Instant fromI = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant toI = to.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        List<ConsumptionReading> readings = readingRepository.findBySensorIdsBetween(sensorIds, fromI, toI);
        return aggregateByDay(readings, from, to);
    }

    public ConsumptionSummaryResource buildSummary(List<Sensor> sensors, LocalDate from, LocalDate to) {
        List<Long> sensorIds = sensors.stream().map(Sensor::getId).collect(Collectors.toList());
        Instant fromI = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant toI = to.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);

        List<ConsumptionReading> readings = sensorIds.isEmpty()
                ? List.of()
                : readingRepository.findBySensorIdsBetween(sensorIds, fromI, toI);

        Map<LocalDate, Double> byDay = new TreeMap<>();
        double total = 0.0;
        for (ConsumptionReading r : readings) {
            LocalDate day = r.getRecordedAt().atZone(ZoneOffset.UTC).toLocalDate();
            byDay.merge(day, r.getLiters(), Double::sum);
            total += r.getLiters();
        }

        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        DateTimeFormatter labelFmt = DateTimeFormatter.ofPattern("d MMM", new Locale("es", "PE"));
        LocalDate cur = from;
        while (!cur.isAfter(to)) {
            labels.add(cur.format(labelFmt));
            values.add(byDay.getOrDefault(cur, 0.0));
            cur = cur.plusDays(1);
        }

        long days = from.until(to, java.time.temporal.ChronoUnit.DAYS) + 1;
        double avgDaily = days > 0 ? total / days : 0.0;

        LocalDate today = LocalDate.now();
        double todayVolume = byDay.getOrDefault(today, 0.0);

        // Previous period
        LocalDate prevFrom = from.minusMonths(1);
        LocalDate prevTo = to.minusMonths(1);
        List<ConsumptionReading> prevReadings = sensorIds.isEmpty()
                ? List.of()
                : readingRepository.findBySensorIdsBetween(sensorIds,
                    prevFrom.atStartOfDay(ZoneOffset.UTC).toInstant(),
                    prevTo.atTime(23,59,59).toInstant(ZoneOffset.UTC));
        double prevTotal = prevReadings.stream().mapToDouble(ConsumptionReading::getLiters).sum();
        double variation = prevTotal > 0 ? ((total - prevTotal) / prevTotal) * 100.0 : 0.0;

        double cost = total * 0.005;

        List<CategoryBreakdownResource> categories = List.of(
                new CategoryBreakdownResource("bathroom", total * 0.40, 40),
                new CategoryBreakdownResource("kitchen", total * 0.30, 30),
                new CategoryBreakdownResource("garden", total * 0.20, 20),
                new CategoryBreakdownResource("laundry", total * 0.10, 10)
        );

        return new ConsumptionSummaryResource(
                total,
                Math.round(variation * 100.0) / 100.0,
                Math.round(cost * 100.0) / 100.0,
                todayVolume,
                Math.round(avgDaily * 100.0) / 100.0,
                categories,
                labels,
                values,
                variation < 0
        );
    }

    private List<ReadingResource> aggregateByDay(List<ConsumptionReading> readings, LocalDate from, LocalDate to) {
        Map<LocalDate, Double> byDay = new TreeMap<>();
        for (ConsumptionReading r : readings) {
            byDay.merge(r.getRecordedAt().atZone(ZoneOffset.UTC).toLocalDate(), r.getLiters(), Double::sum);
        }
        List<ReadingResource> result = new ArrayList<>();
        LocalDate cur = from;
        while (!cur.isAfter(to)) {
            result.add(new ReadingResource(cur.toString(), byDay.getOrDefault(cur, 0.0)));
            cur = cur.plusDays(1);
        }
        return result;
    }
}
