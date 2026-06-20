package com.hydrosmart.backend.incident.infrastructure.interfaces.rest;

import com.hydrosmart.backend.incident.application.internal.commandservices.IncidentCommandService;
import com.hydrosmart.backend.incident.infrastructure.interfaces.rest.resources.AlertResource;
import com.hydrosmart.backend.incident.infrastructure.interfaces.rest.transform.AlertResourceAssembler;
import com.hydrosmart.backend.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Incident")
public class IncidentController {

    private final IncidentCommandService incidentCommandService;

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<AlertResource>>> getAlerts(
            @RequestParam(required = false) Long buildingId,
            @RequestParam(required = false) Long unitId,
            @RequestParam(required = false) String status) {

        List<AlertResource> resources;
        if (buildingId != null) {
            resources = incidentCommandService.getAlertsByBuilding(buildingId, status).stream()
                    .map(AlertResourceAssembler::toResource).collect(Collectors.toList());
        } else if (unitId != null) {
            resources = incidentCommandService.getAlertsByUnit(unitId).stream()
                    .map(AlertResourceAssembler::toResource).collect(Collectors.toList());
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("Debe especificar buildingId o unitId"));
        }
        return ResponseEntity.ok(ApiResponse.ok(resources));
    }

    @PutMapping("/alerts/{id}/resolve")
    @PreAuthorize("hasRole('BUILDING_ADMIN')")
    public ResponseEntity<ApiResponse<AlertResource>> resolve(@PathVariable Long id) {
        var alert = incidentCommandService.resolveAlert(id);
        return ResponseEntity.ok(ApiResponse.ok(AlertResourceAssembler.toResource(alert)));
    }
}
