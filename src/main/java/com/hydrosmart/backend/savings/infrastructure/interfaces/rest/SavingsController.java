package com.hydrosmart.backend.savings.infrastructure.interfaces.rest;

import com.hydrosmart.backend.property.application.internal.commandservices.PropertyCommandService;
import com.hydrosmart.backend.property.domain.model.aggregates.Unit;
import com.hydrosmart.backend.savings.application.internal.commandservices.SavingsCommandService;
import com.hydrosmart.backend.savings.infrastructure.interfaces.rest.resources.ReportResource;
import com.hydrosmart.backend.savings.infrastructure.interfaces.rest.resources.SavingGoalResource;
import com.hydrosmart.backend.savings.infrastructure.interfaces.rest.transform.SavingGoalResourceAssembler;
import com.hydrosmart.backend.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Savings")
public class SavingsController {

    private final SavingsCommandService savingsCommandService;
    private final PropertyCommandService propertyCommandService;

    @GetMapping("/saving-goals/active")
    public ResponseEntity<ApiResponse<SavingGoalResource>> getActiveGoal(@RequestParam Long buildingId) {
        return ResponseEntity.ok(ApiResponse.ok(
                SavingGoalResourceAssembler.toResource(savingsCommandService.getActiveGoal(buildingId))));
    }

    @GetMapping("/reports/monthly")
    @PreAuthorize("hasRole('BUILDING_ADMIN')")
    public ResponseEntity<ApiResponse<ReportResource>> getMonthlyReport(
            @RequestParam Long buildingId, @RequestParam String period) {
        return ResponseEntity.ok(ApiResponse.ok(savingsCommandService.getMonthlyReport(buildingId, period)));
    }

    /** Reporte mensual de una unidad específica (tenant-safe: verifica propiedad). */
    @GetMapping("/reports/unit/{id}/monthly")
    public ResponseEntity<ApiResponse<ReportResource>> getMonthlyUnitReport(
            @PathVariable Long id, @RequestParam String period,
            @AuthenticationPrincipal UserDetails userDetails) {
        Unit unit = propertyCommandService.getUnit(id);
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_BUILDING_ADMIN"));
        Long currentUserId = propertyCommandService.getUserIdByEmail(userDetails.getUsername()).orElse(null);
        if (!isAdmin && (unit.getTenantUserId() == null || !unit.getTenantUserId().equals(currentUserId))) {
            return ResponseEntity.status(403).body(ApiResponse.error("Acceso denegado"));
        }
        return ResponseEntity.ok(ApiResponse.ok(savingsCommandService.getMonthlyReportForUnit(id, period)));
    }
}
