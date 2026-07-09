package com.hydrosmart.backend.irrigation.domain.model.aggregates;

/**
 * Aggregate que representa la recomendación de riego generada por el
 * bounded context Smart Irrigation a partir de los datos del clima.
 */
public class IrrigationRecommendation {

    private final boolean shouldIrrigate;
    private final String recommendation;
    private final double rainMmNext24h;
    private final double temperatureC;
    private final String weatherCondition;
    private final double latitude;
    private final double longitude;

    public IrrigationRecommendation(boolean shouldIrrigate, String recommendation,
                                    double rainMmNext24h, double temperatureC,
                                    String weatherCondition, double latitude, double longitude) {
        this.shouldIrrigate = shouldIrrigate;
        this.recommendation = recommendation;
        this.rainMmNext24h = rainMmNext24h;
        this.temperatureC = temperatureC;
        this.weatherCondition = weatherCondition;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isShouldIrrigate() { return shouldIrrigate; }
    public String getRecommendation() { return recommendation; }
    public double getRainMmNext24h() { return rainMmNext24h; }
    public double getTemperatureC() { return temperatureC; }
    public String getWeatherCondition() { return weatherCondition; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
