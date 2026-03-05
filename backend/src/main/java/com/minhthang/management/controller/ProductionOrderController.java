package com.minhthang.management.controller;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.ProductionOrder;
import com.minhthang.management.entity.ProductionStatus;
import com.minhthang.management.service.ProductionOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/production-orders")
@RequiredArgsConstructor
public class ProductionOrderController {

    private final ProductionOrderService productionOrderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductionOrder>>> getAll(@RequestParam(required = false) String status) {
        ApiResponse<List<ProductionOrder>> response;
        if (status != null && !status.isEmpty()) {
            response = productionOrderService.getByStatus(ProductionStatus.valueOf(status));
        } else {
            response = productionOrderService.getAll();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductionOrder>> getById(@PathVariable String id) {
        ApiResponse<ProductionOrder> response = productionOrderService.getById(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductionOrder>> create(@RequestBody ProductionOrder productionOrder) {
        return ResponseEntity.ok(productionOrderService.create(productionOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductionOrder>> update(@PathVariable String id, @RequestBody ProductionOrder productionOrder) {
        ApiResponse<ProductionOrder> response = productionOrderService.update(id, productionOrder);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ProductionOrder>> changeStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        ProductionStatus status = ProductionStatus.valueOf(body.get("status"));
        ApiResponse<ProductionOrder> response = productionOrderService.changeStatus(id, status);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}/progress")
    public ResponseEntity<ApiResponse<ProductionOrder>> updateProgress(@PathVariable String id, @RequestBody Map<String, Object> body) {
        BigDecimal completedQuantity = new BigDecimal(body.get("completedQuantity").toString());
        BigDecimal scrapQuantity = new BigDecimal(body.get("scrapQuantity").toString());
        ApiResponse<ProductionOrder> response = productionOrderService.updateProgress(id, completedQuantity, scrapQuantity);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
