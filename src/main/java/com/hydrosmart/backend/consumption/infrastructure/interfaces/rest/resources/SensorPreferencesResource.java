package com.hydrosmart.backend.consumption.infrastructure.interfaces.rest.resources;

public record SensorPreferencesResource(
        boolean detectLeaks,
        boolean trackDailyConsumption,
        boolean trackMonthlyConsumption,
        boolean sendAlertsOnAnomaly,
        boolean sendWeeklyReports,
        boolean sendMonthlyReports,
        boolean enableEnergyTracking,
        boolean alertOnHighPressure
) {}
