package com.minhthang.management.controller;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }

    @GetMapping("/monitoring")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMonitoring() {
        return ResponseEntity.ok(dashboardService.getMonitoring());
    }

    @GetMapping("/sales-staff")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSalesStaff() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(dashboardService.getSalesStaffDashboard(email));
    }

    @GetMapping("/sales-manager")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSalesManager() {
        return ResponseEntity.ok(dashboardService.getSalesManagerDashboard());
    }

    @GetMapping("/production")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProduction() {
        return ResponseEntity.ok(dashboardService.getProductionDashboard());
    }

    @GetMapping("/warehouse")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getWarehouse() {
        return ResponseEntity.ok(dashboardService.getWarehouseDashboard());
    }
}
