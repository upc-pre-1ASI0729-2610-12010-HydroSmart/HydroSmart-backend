package com.hydrosmart.backend.consumption.domain.model.valueobjects;

public record SensorPreferences(
        boolean detectLeaks,
        boolean trackDailyConsumption,
        boolean trackMonthlyConsumption,
        boolean sendAlertsOnAnomaly,
        boolean sendWeeklyReports,
        boolean sendMonthlyReports,
        boolean enableEnergyTracking,
        boolean alertOnHighPressure
) {
    public static SensorPreferences defaults() {
        return new SensorPreferences(true, true, true, true, true, true, false, true);
    }
}
