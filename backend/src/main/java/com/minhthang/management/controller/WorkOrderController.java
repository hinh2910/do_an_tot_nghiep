package com.minhthang.management.controller;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.WorkOrder;
import com.minhthang.management.entity.WorkOrderStatus;
import com.minhthang.management.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkOrder>>> getAll(
            @RequestParam(required = false) String productionOrderId,
            @RequestParam(required = false) String status) {
        ApiResponse<List<WorkOrder>> response;
        if (productionOrderId != null && !productionOrderId.isEmpty()) {
            response = workOrderService.getByProductionOrderId(productionOrderId);
        } else if (status != null && !status.isEmpty()) {
            response = workOrderService.getByStatus(WorkOrderStatus.valueOf(status));
        } else {
            response = workOrderService.getAll();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkOrder>> getById(@PathVariable String id) {
        ApiResponse<WorkOrder> response = workOrderService.getById(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WorkOrder>> create(@RequestBody WorkOrder workOrder) {
        return ResponseEntity.ok(workOrderService.create(workOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkOrder>> update(@PathVariable String id, @RequestBody WorkOrder workOrder) {
        ApiResponse<WorkOrder> response = workOrderService.update(id, workOrder);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<WorkOrder>> changeStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        WorkOrderStatus status = WorkOrderStatus.valueOf(body.get("status"));
        ApiResponse<WorkOrder> response = workOrderService.changeStatus(id, status);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}/progress")
    public ResponseEntity<ApiResponse<WorkOrder>> updateProgress(@PathVariable String id, @RequestBody Map<String, Object> body) {
        BigDecimal completedQuantity = new BigDecimal(body.get("completedQuantity").toString());
        BigDecimal scrapQuantity = new BigDecimal(body.get("scrapQuantity").toString());
        ApiResponse<WorkOrder> response = workOrderService.updateProgress(id, completedQuantity, scrapQuantity);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
