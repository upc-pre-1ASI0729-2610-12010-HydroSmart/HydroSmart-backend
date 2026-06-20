package com.hydrosmart.backend.savings.infrastructure.interfaces.rest.resources;

import java.util.List;

public record ReportResource(
        Double totalVolume,
        Double averageDailyVolume,
        String peakDay,
        Double estimatedCost,
        List<DeviceRankingResource> deviceRanking,
        List<WeeklyAverageResource> weeklyAverages
) {
    public record DeviceRankingResource(Long sensorId, String sensorName, Double volume, Integer percentage, Integer rank) {}
    public record WeeklyAverageResource(String week, Double average) {}
}
