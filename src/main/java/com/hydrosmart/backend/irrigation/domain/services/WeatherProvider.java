package com.hydrosmart.backend.irrigation.domain.services;

/**
 * Port (interface) del proveedor de información meteorológica.
 * Define el contrato del servicio externo de clima, de modo que el dominio
 * no dependa de un proveedor concreto (OpenWeather).
 */
public interface WeatherProvider {

    WeatherData fetch(double latitude, double longitude);

    /**
     * Datos meteorológicos relevantes para la recomendación de riego.
     *
     * @param rainMmNext24h precipitación acumulada estimada para las próximas 24h (mm).
     * @param temperatureC  temperatura actual (°C).
     * @param condition      descripción del clima (ej. "light rain").
     */
    record WeatherData(double rainMmNext24h, double temperatureC, String condition) {}
}
