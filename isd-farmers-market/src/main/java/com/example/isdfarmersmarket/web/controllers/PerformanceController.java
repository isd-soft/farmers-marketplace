package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.PerformanceService;
import com.example.isdfarmersmarket.dao.projections.MonthlyRevenueByCategoryProjection;
import com.example.isdfarmersmarket.dao.projections.MonthlyRevenueProjection;
import com.example.isdfarmersmarket.dao.projections.RevenueByCategoryProjection;
import com.example.isdfarmersmarket.dao.projections.RevenueByProductProjection;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasRole('FARMER')")
@RequiredArgsConstructor
@RestController
@RequestMapping("/performance")
public class PerformanceController {

    private final PerformanceService performanceService;
    @Operation(
            description = "This endpoint retrieves the total revenue by category for a specific year."
    )
    @GetMapping("category/revenue-sum")
    public List<RevenueByCategoryProjection> getRevenueByCategory(@RequestParam Long year) {
        return performanceService.getRevenueByCategory(year);
    }

    @Operation(
            description = "This endpoint retrieves the total revenue by product for a specific year."
    )
    @GetMapping("product/revenue-sum")
    public List<RevenueByProductProjection> getRevenueByProduct(@RequestParam Long year) {
        return performanceService.getRevenueByProduct(year);
    }

    @Operation(
            description = "This endpoint retrieves the monthly revenue for a specific year."
    )
    @GetMapping("/month/revenue")
    public List<MonthlyRevenueProjection> getMonthlyRevenue(@RequestParam Long year) {
        return performanceService.getMonthlyRevenue(year);
    }

    @Operation(
            description = "This endpoint retrieves the monthly revenue by category for a specific year."
    )
    @GetMapping("/month/category-revenue")
    public List<MonthlyRevenueByCategoryProjection> getMonthlyRevenueByCategory(@RequestParam Long year) {
        return performanceService.getMonthlyRevenueByCategory(year);
    }

    @Operation(
            description = "This endpoint retrieves the total revenue for a specific year."
    )
    @GetMapping("/revenue")
    public Long getTotalRevenue(@RequestParam Long year) {
        return performanceService.getTotalRevenue(year);
    }
}


