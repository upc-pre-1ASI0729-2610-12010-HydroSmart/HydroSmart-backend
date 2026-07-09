package com.hydrosmart.backend.irrigation.application.internal.commandservices;

import com.hydrosmart.backend.irrigation.domain.model.aggregates.IrrigationRecommendation;
import com.hydrosmart.backend.irrigation.domain.services.WeatherProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación del bounded context Smart Irrigation.
 * Combina los datos del clima (proveedor externo) con la lógica de negocio
 * para generar una recomendación de riego.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IrrigationCommandService {

    private final WeatherProvider weatherProvider;

    @Value("${app.irrigation.default-lat:-12.05}")
    private double defaultLat;

    @Value("${app.irrigation.default-lon:-77.04}")
    private double defaultLon;

    // Umbrales de precipitación (mm en 24h) que definen la recomendación.
    private static final double NO_IRRIGATION_THRESHOLD = 8.0;
    private static final double REDUCED_IRRIGATION_THRESHOLD = 3.0;

    /**
     * Genera una recomendación de riego basada en el clima de la ubicación dada.
     *
     * @param latitude  latitud; si es null o cero, usa el default (Lima).
     * @param longitude longitud; si es null o cero, usa el default (Lima).
     */
    public IrrigationRecommendation getRecommendation(Double latitude, Double longitude) {
        double lat = (latitude == null || latitude == 0.0) ? defaultLat : latitude;
        double lon = (longitude == null || longitude == 0.0) ? defaultLon : longitude;

        WeatherProvider.WeatherData weather = weatherProvider.fetch(lat, lon);

        boolean shouldIrrigate;
        String recommendation;

        if (weather.rainMmNext24h() >= NO_IRRIGATION_THRESHOLD) {
            shouldIrrigate = false;
            recommendation = String.format(
                    "No es necesario regar hoy. Se esperan %.1f mm de lluvia en las próximas 24 horas.",
                    weather.rainMmNext24h());
        } else if (weather.rainMmNext24h() >= REDUCED_IRRIGATION_THRESHOLD) {
            shouldIrrigate = true;
            recommendation = String.format(
                    "Riego reducido recomendado. Se esperan %.1f mm de lluvia; ajusta el riego a la mitad.",
                    weather.rainMmNext24h());
        } else {
            shouldIrrigate = true;
            recommendation = String.format(
                    "Riego recomendado. Sin lluvia significativa esperada (%.1f mm en 24h). "
                            + "Temperatura actual: %.1f°C.",
                    weather.rainMmNext24h(), weather.temperatureC());
        }

        log.info("[Irrigation] Recommendation at ({},{}): irrigate={}, rain={}mm",
                lat, lon, shouldIrrigate, weather.rainMmNext24h());

        return new IrrigationRecommendation(
                shouldIrrigate, recommendation,
                weather.rainMmNext24h(), weather.temperatureC(),
                weather.condition(), lat, lon);
    }
}
