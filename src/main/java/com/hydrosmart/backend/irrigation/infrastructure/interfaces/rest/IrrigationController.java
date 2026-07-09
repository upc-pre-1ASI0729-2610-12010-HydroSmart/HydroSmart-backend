package com.hydrosmart.backend.irrigation.infrastructure.interfaces.rest;

import com.hydrosmart.backend.irrigation.application.internal.commandservices.IrrigationCommandService;
import com.hydrosmart.backend.irrigation.domain.model.aggregates.IrrigationRecommendation;
import com.hydrosmart.backend.irrigation.infrastructure.interfaces.rest.resources.IrrigationRecommendationResource;
import com.hydrosmart.backend.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST del bounded context Smart Irrigation.
 * Integra el servicio externo OpenWeather para recomendar el riego según el clima.
 */
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Tag(name = "Smart Irrigation")
public class IrrigationController {

    private final IrrigationCommandService irrigationCommandService;

    /**
     * Devuelve una recomendación de riego basada en las condiciones meteorológicas.
     *
     * @param latitude  latitud (opcional; por defecto Lima).
     * @param longitude longitud (opcional; por defecto Lima).
     */
    @GetMapping("/recommendation")
    @Operation(summary = "Get a weather-based irrigation recommendation via OpenWeather")
    public ResponseEntity<ApiResponse<IrrigationRecommendationResource>> recommendation(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {
        IrrigationRecommendation rec = irrigationCommandService.getRecommendation(latitude, longitude);
        IrrigationRecommendationResource resource = new IrrigationRecommendationResource(
                rec.isShouldIrrigate(), rec.getRecommendation(),
                rec.getRainMmNext24h(), rec.getTemperatureC(),
                rec.getWeatherCondition(), rec.getLatitude(), rec.getLongitude());
        return ResponseEntity.ok(ApiResponse.ok(resource));
    }
}
