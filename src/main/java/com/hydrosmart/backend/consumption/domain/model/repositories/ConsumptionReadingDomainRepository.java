package com.hydrosmart.backend.consumption.domain.model.repositories;

import com.hydrosmart.backend.consumption.domain.model.aggregates.ConsumptionReading;

import java.time.Instant;
import java.util.List;

public interface ConsumptionReadingDomainRepository {
    List<ConsumptionReading> findBySensorIdBetween(Long sensorId, Instant from, Instant to);
    List<ConsumptionReading> findBySensorIdsBetween(List<Long> sensorIds, Instant from, Instant to);
    ConsumptionReading save(ConsumptionReading reading);
    void saveAll(List<ConsumptionReading> readings);
}
