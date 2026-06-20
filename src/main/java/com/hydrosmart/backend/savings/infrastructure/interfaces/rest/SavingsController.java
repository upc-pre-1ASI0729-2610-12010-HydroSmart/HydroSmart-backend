package com.hydrosmart.backend.savings.infrastructure.interfaces.rest;

import com.hydrosmart.backend.savings.application.internal.commandservices.SavingsCommandService;
import com.hydrosmart.backend.savings.infrastructure.interfaces.rest.resources.ReportResource;
import com.hydrosmart.backend.savings.infrastructure.interfaces.rest.resources.SavingGoalResource;
import com.hydrosmart.backend.savings.infrastructure.interfaces.rest.transform.SavingGoalResourceAssembler;
import com.hydrosmart.backend.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Savings")
public class SavingsController {

    private final SavingsCommandService savingsCommandService;

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
}
