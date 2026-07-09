package com.hydrosmart.backend.property.infrastructure.interfaces.rest;

import com.hydrosmart.backend.consumption.application.internal.commandservices.ConsumptionCommandService;
import com.hydrosmart.backend.consumption.domain.model.aggregates.Sensor;
import com.hydrosmart.backend.consumption.infrastructure.interfaces.rest.resources.ConsumptionSummaryResource;
import com.hydrosmart.backend.iam.domain.model.aggregates.User;
import com.hydrosmart.backend.property.application.internal.commandservices.PropertyCommandService;
import com.hydrosmart.backend.property.domain.model.aggregates.Unit;
import com.hydrosmart.backend.property.infrastructure.interfaces.rest.resources.*;
import com.hydrosmart.backend.property.infrastructure.interfaces.rest.transform.BuildingResourceAssembler;
import com.hydrosmart.backend.property.infrastructure.interfaces.rest.transform.TenantDetailResourceAssembler;
import com.hydrosmart.backend.property.infrastructure.interfaces.rest.transform.UnitResourceAssembler;
import com.hydrosmart.backend.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Property")
public class PropertyController {

    private final PropertyCommandService propertyCommandService;
    private final ConsumptionCommandService consumptionCommandService;

    @GetMapping("/buildings/{id}")
    @PreAuthorize("hasRole('BUILDING_ADMIN')")
    public ResponseEntity<ApiResponse<BuildingResource>> getBuilding(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(
                BuildingResourceAssembler.toResource(propertyCommandService.getBuilding(id))));
    }

    @GetMapping("/buildings/{id}/summary")
    @PreAuthorize("hasRole('BUILDING_ADMIN')")
    public ResponseEntity<ApiResponse<ConsumptionSummaryResource>> getBuildingSummary(@PathVariable Long id) {
        List<Sensor> sensors = consumptionCommandService.getSensorsByBuilding(id);
        LocalDate now = LocalDate.now();
        LocalDate from = now.withDayOfMonth(1);
        LocalDate to = now.withDayOfMonth(now.lengthOfMonth());
        return ResponseEntity.ok(ApiResponse.ok(consumptionCommandService.buildSummary(sensors, from, to)));
    }

    @GetMapping("/units")
    @PreAuthorize("hasRole('BUILDING_ADMIN')")
    public ResponseEntity<ApiResponse<List<UnitResource>>> getUnits(@RequestParam Long buildingId) {
        List<UnitResource> resources = propertyCommandService.getUnitsByBuilding(buildingId).stream()
                .map(u -> UnitResourceAssembler.toResource(u, propertyCommandService.resolveTenantName(u.getTenantUserId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(resources));
    }

    @GetMapping("/units/{id}")
    public ResponseEntity<ApiResponse<UnitResource>> getUnit(@PathVariable Long id) {
        Unit unit = propertyCommandService.getUnit(id);
        return ResponseEntity.ok(ApiResponse.ok(
                UnitResourceAssembler.toResource(unit, propertyCommandService.resolveTenantName(unit.getTenantUserId()))));
    }

    @GetMapping("/units/{id}/summary")
    public ResponseEntity<ApiResponse<ConsumptionSummaryResource>> getUnitSummary(
            @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        Unit unit = propertyCommandService.getUnit(id);
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_BUILDING_ADMIN"));
        Long currentUserId = propertyCommandService.getUserIdByEmail(userDetails.getUsername()).orElse(null);
        // Un tenant solo puede ver el resumen de SU unidad
        if (!isAdmin && (unit.getTenantUserId() == null || !unit.getTenantUserId().equals(currentUserId))) {
            return ResponseEntity.status(403).body(ApiResponse.error("Acceso denegado"));
        }

        List<Sensor> sensors = consumptionCommandService.getSensorsByUnit(id);
        LocalDate now = LocalDate.now();
        LocalDate from = now.withDayOfMonth(1);
        LocalDate to = now.withDayOfMonth(now.lengthOfMonth());
        return ResponseEntity.ok(ApiResponse.ok(consumptionCommandService.buildSummary(sensors, from, to)));
    }

    /** Endpoint tenant-safe: devuelve la unidad del usuario autenticado. */
    @GetMapping("/units/me")
    public ResponseEntity<ApiResponse<UnitResource>> getMyUnit(@AuthenticationPrincipal UserDetails userDetails) {
        return propertyCommandService.getUnitForTenantEmail(userDetails.getUsername())
                .map(u -> ResponseEntity.ok(ApiResponse.ok(
                        UnitResourceAssembler.toResource(u, propertyCommandService.resolveTenantName(u.getTenantUserId())))))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(ApiResponse.error("No tienes una unidad asignada")));
    }

    @GetMapping("/units/{id}/tenant")
    @PreAuthorize("hasRole('BUILDING_ADMIN')")
    public ResponseEntity<ApiResponse<TenantDetailResource>> getTenant(@PathVariable Long id) {
        User tenant = propertyCommandService.getTenantOfUnit(id);
        Unit unit = propertyCommandService.getUnit(id);
        return ResponseEntity.ok(ApiResponse.ok(TenantDetailResourceAssembler.toResource(tenant, unit)));
    }

    @PostMapping("/units/{id}/assign-tenant")
    @PreAuthorize("hasRole('BUILDING_ADMIN')")
    public ResponseEntity<ApiResponse<UnitResource>> assignTenant(
            @PathVariable Long id, @Valid @RequestBody AssignTenantRequestResource request) {
        Unit unit = propertyCommandService.assignTenant(id, request);
        return ResponseEntity.ok(ApiResponse.ok(
                UnitResourceAssembler.toResource(unit, propertyCommandService.resolveTenantName(unit.getTenantUserId()))));
    }

    @DeleteMapping("/units/{id}/tenant")
    @PreAuthorize("hasRole('BUILDING_ADMIN')")
    public ResponseEntity<ApiResponse<UnitResource>> removeTenant(@PathVariable Long id) {
        Unit unit = propertyCommandService.removeTenant(id);
        return ResponseEntity.ok(ApiResponse.ok(UnitResourceAssembler.toResource(unit, "")));
    }
}
