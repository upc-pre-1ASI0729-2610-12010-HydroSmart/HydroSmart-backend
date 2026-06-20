package com.hydrosmart.backend.consumption.infrastructure.interfaces.rest.resources;

import java.util.List;

public record ConsumptionSummaryResource(
        Double totalVolumeLiters,
        Double variationPercent,
        Double estimatedCostPEN,
        Double currentDayVolumeLiters,
        Double averageDailyVolumeLiters,
        List<CategoryBreakdownResource> categoryBreakdown,
        List<String> dailyLabels,
        List<Double> dailyValues,
        Boolean isDecreasing
) {}
