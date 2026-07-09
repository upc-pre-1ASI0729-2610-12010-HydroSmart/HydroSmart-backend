package com.hydrosmart.backend.irrigation.infrastructure.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.hydrosmart.backend.irrigation.domain.services.WeatherProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

/**
 * Adapter que implementa {@link WeatherProvider} usando el servicio externo
 * OpenWeather. Consulta el endpoint de pronóstico (5 días / 3 horas) y agrega
 * la precipitación esperada para las próximas 24 horas.
 *
 * Endpoint: https://api.openweathermap.org/data/2.5/forecast
 */
@Component
@Slf4j
public class OpenWeatherAdapter implements WeatherProvider {

    private final RestClient weatherClient;
    private final String apiKey;

    public OpenWeatherAdapter(
            @Value("${openweather.api.url:https://api.openweathermap.org/data/2.5/forecast}") String apiUrl,
            @Value("${openweather.api.key:}") String apiKey,
            RestClient.Builder restClientBuilder) {
        this.apiKey = apiKey;
        this.weatherClient = restClientBuilder.baseUrl(apiUrl).build();
    }

    @Override
    public WeatherData fetch(double latitude, double longitude) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("[OpenWeather] No API key configured (openweather.api.key). Returning empty data.");
            return new WeatherData(0.0, 0.0, "API key not configured");
        }
        try {
            JsonNode root = weatherClient.get()
                    .uri(uri -> uri
                            .queryParam("lat", latitude)
                            .queryParam("lon", longitude)
                            .queryParam("appid", apiKey)
                            .queryParam("units", "metric")
                            .queryParam("lang", "en")
                            .build())
                    .retrieve()
                    .body(JsonNode.class);

            if (root == null || !root.has("list")) {
                return new WeatherData(0.0, 0.0, "no data");
            }

            JsonNode list = root.get("list");
            double totalRain = 0.0;
            double temp = 0.0;
            String condition = "clear";

            // Primeras 8 entradas = próximas 24h (intervalos de 3h)
            int limit = Math.min(8, list.size());
            for (int i = 0; i < limit; i++) {
                JsonNode entry = list.get(i);
                if (i == 0) {
                    temp = pathDouble(entry, "main", "temp");
                    JsonNode weather = entry.path("weather");
                    if (weather.isArray() && weather.size() > 0) {
                        condition = weather.get(0).path("description").asText("clear");
                    }
                }
                totalRain += pathDouble(entry, "rain", "3h");
            }

            log.info("[OpenWeather] lat={},lon={} -> rain24h={}mm, temp={}°C, cond={}",
                    latitude, longitude, totalRain, temp, condition);
            return new WeatherData(round(totalRain), round(temp), condition);
        } catch (Exception e) {
            log.error("[OpenWeather] Error fetching weather: {}", e.getMessage());
            return new WeatherData(0.0, 0.0, "error: " + e.getMessage());
        }
    }

    private double pathDouble(JsonNode node, String first, String second) {
        return Optional.ofNullable(node)
                .map(n -> n.path(first))
                .map(n -> n.path(second))
                .filter(n -> !n.isMissingNode())
                .map(JsonNode::asDouble)
                .orElse(0.0);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
