package com.hydrosmart.backend.savings.application.internal.commandservices;

import com.hydrosmart.backend.consumption.domain.model.aggregates.ConsumptionReading;
import com.hydrosmart.backend.consumption.domain.model.aggregates.Sensor;
import com.hydrosmart.backend.consumption.domain.model.repositories.ConsumptionReadingDomainRepository;
import com.hydrosmart.backend.consumption.domain.model.repositories.SensorDomainRepository;
import com.hydrosmart.backend.savings.domain.model.aggregates.SavingGoal;
import com.hydrosmart.backend.savings.domain.model.repositories.SavingGoalDomainRepository;
import com.hydrosmart.backend.savings.infrastructure.interfaces.rest.resources.ReportResource;
import com.hydrosmart.backend.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavingsCommandService {

    private final SavingGoalDomainRepository savingGoalRepository;
    private final SensorDomainRepository sensorRepository;
    private final ConsumptionReadingDomainRepository readingRepository;

    public SavingGoal getActiveGoal(Long buildingId) {
        return savingGoalRepository.findByBuildingIdAndStatus(buildingId, "active")
                .orElseThrow(() -> new ResourceNotFoundException("No hay meta activa para el edificio " + buildingId));
    }

    public ReportResource getMonthlyReport(Long buildingId, String period) {
        YearMonth ym = YearMonth.parse(period);
        LocalDate from = ym.atDay(1);
        LocalDate to = ym.atEndOfMonth();

        List<Sensor> sensors = sensorRepository.findByBuildingId(buildingId);
        List<Long> sensorIds = sensors.stream().map(Sensor::getId).collect(Collectors.toList());

        Instant fromI = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant toI = to.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);

        List<ConsumptionReading> readings = sensorIds.isEmpty()
                ? List.of()
                : readingRepository.findBySensorIdsBetween(sensorIds, fromI, toI);

        double total = readings.stream().mapToDouble(ConsumptionReading::getLiters).sum();
        long days = from.until(to, java.time.temporal.ChronoUnit.DAYS) + 1;
        double avgDaily = days > 0 ? total / days : 0.0;

        Map<LocalDate, Double> byDay = new TreeMap<>();
        for (ConsumptionReading r : readings) {
            byDay.merge(r.getRecordedAt().atZone(ZoneOffset.UTC).toLocalDate(), r.getLiters(), Double::sum);
        }
        String peakDay = byDay.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey().toString())
                .orElse(null);

        double estimatedCost = total * 0.005;

        Map<Long, Double> bySensor = new HashMap<>();
        for (ConsumptionReading r : readings) {
            bySensor.merge(r.getSensorId(), r.getLiters(), Double::sum);
        }
        Map<Long, Sensor> sensorMap = sensors.stream()
                .collect(Collectors.toMap(Sensor::getId, s -> s));

        List<Map.Entry<Long, Double>> sorted = new ArrayList<>(bySensor.entrySet());
        sorted.sort(Map.Entry.<Long, Double>comparingByValue().reversed());

        List<ReportResource.DeviceRankingResource> ranking = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i++) {
            Long sid = sorted.get(i).getKey();
            Double vol = sorted.get(i).getValue();
            Sensor s = sensorMap.get(sid);
            int pct = total > 0 ? (int) Math.round((vol / total) * 100) : 0;
            ranking.add(new ReportResource.DeviceRankingResource(sid, s != null ? s.getName() : "", vol, pct, i + 1));
        }

        List<ReportResource.WeeklyAverageResource> weeklyAvgs = new ArrayList<>();
        LocalDate weekStart = from;
        int weekNum = 1;
        while (!weekStart.isAfter(to)) {
            LocalDate weekEnd = weekStart.plusDays(6).isAfter(to) ? to : weekStart.plusDays(6);
            final LocalDate ws = weekStart;
            final LocalDate we = weekEnd;
            double weekTotal = byDay.entrySet().stream()
                    .filter(e -> !e.getKey().isBefore(ws) && !e.getKey().isAfter(we))
                    .mapToDouble(Map.Entry::getValue).sum();
            long weekDays = ws.until(we, java.time.temporal.ChronoUnit.DAYS) + 1;
            weeklyAvgs.add(new ReportResource.WeeklyAverageResource("Semana " + weekNum,
                    weekDays > 0 ? weekTotal / weekDays : 0.0));
            weekStart = weekEnd.plusDays(1);
            weekNum++;
        }

        return new ReportResource(total, avgDaily, peakDay, estimatedCost, ranking, weeklyAvgs);
    }
}
