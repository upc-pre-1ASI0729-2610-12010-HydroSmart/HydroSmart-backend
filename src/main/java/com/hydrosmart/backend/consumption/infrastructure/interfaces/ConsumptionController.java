package com.hydrosmart.backend.consumption.infrastructure.interfaces.rest;

import com.hydrosmart.backend.consumption.application.internal.commandservices.ConsumptionCommandService;
import com.hydrosmart.backend.consumption.domain.model.aggregates.Sensor;
import com.hydrosmart.backend.consumption.infrastructure.interfaces.rest.resources.*;
import com.hydrosmart.backend.consumption.infrastructure.interfaces.rest.transform.SensorResourceAssembler;
import com.hydrosmart.backend.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Consumption")
public class ConsumptionController {

    private final ConsumptionCommandService consumptionCommandService;

    @GetMapping("/sensors")
    public ResponseEntity<ApiResponse<List<SensorResource>>> getSensors(
            @RequestParam(required = false) Long buildingId,
            @RequestParam(required = false) Long unitId) {

        List<Sensor> sensors;
        if (buildingId != null) sensors = consumptionCommandService.getSensorsByBuilding(buildingId);
        else if (unitId != null) sensors = consumptionCommandService.getSensorsByUnit(unitId);
        else return ResponseEntity.badRequest().body(ApiResponse.error("Debe especificar buildingId o unitId"));

        List<SensorResource> resources = sensors.stream()
                .map(s -> SensorResourceAssembler.toResource(s,
                        consumptionCommandService.unresolvedAlertCount(s.getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(resources));
    }

    @PostMapping("/sensors")
    @PreAuthorize("hasRole('BUILDING_ADMIN')")
    public ResponseEntity<ApiResponse<SensorResource>> createSensor(@Valid @RequestBody CreateSensorRequestResource req) {
        Sensor created = consumptionCommandService.createSensor(req);
        return ResponseEntity.ok(ApiResponse.ok(SensorResourceAssembler.toResource(created, 0)));
    }

    @PutMapping("/sensors/{id}/preferences")
    @PreAuthorize("hasRole('BUILDING_ADMIN')")
    public ResponseEntity<ApiResponse<SensorResource>> updatePreferences(
            @PathVariable Long id, @RequestBody SensorPreferencesResource req) {
        Sensor updated = consumptionCommandService.updatePreferences(id, req);
        return ResponseEntity.ok(ApiResponse.ok(SensorResourceAssembler.toResource(updated,
                consumptionCommandService.unresolvedAlertCount(id))));
    }

    @PutMapping("/sensors/{id}/status")
    @PreAuthorize("hasRole('BUILDING_ADMIN')")
    public ResponseEntity<ApiResponse<SensorResource>> updateStatus(
            @PathVariable Long id, @Valid @RequestBody UpdateSensorStatusRequestResource req) {
        Sensor updated = consumptionCommandService.updateStatus(id, req.status());
        return ResponseEntity.ok(ApiResponse.ok(SensorResourceAssembler.toResource(updated,
                consumptionCommandService.unresolvedAlertCount(id))));
    }

    @GetMapping("/sensors/{id}/readings")
    public ResponseEntity<ApiResponse<List<ReadingResource>>> getSensorReadings(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        LocalDate now = LocalDate.now();
        if (from == null) from = now.withDayOfMonth(1);
        if (to == null) to = now.withDayOfMonth(now.lengthOfMonth());
        return ResponseEntity.ok(ApiResponse.ok(consumptionCommandService.getSensorReadings(id, from, to)));
    }

    @GetMapping("/buildings/{id}/readings")
    @PreAuthorize("hasRole('BUILDING_ADMIN')")
    public ResponseEntity<ApiResponse<List<ReadingResource>>> getBuildingReadings(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        LocalDate now = LocalDate.now();
        if (from == null) from = now.withDayOfMonth(1);
        if (to == null) to = now.withDayOfMonth(now.lengthOfMonth());
        return ResponseEntity.ok(ApiResponse.ok(consumptionCommandService.getBuildingReadings(id, from, to)));
    }
}
