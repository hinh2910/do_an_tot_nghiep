package com.minhthang.management.controller;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.OrderStatus;
import com.minhthang.management.entity.Payment;
import com.minhthang.management.entity.SalesOrder;
import com.minhthang.management.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String customerId) {
        ApiResponse<List<SalesOrder>> response;
        if (status != null && !status.isEmpty()) {
            response = salesOrderService.getByStatus(OrderStatus.valueOf(status));
        } else if (customerId != null && !customerId.isEmpty()) {
            response = salesOrderService.getByCustomerId(customerId);
        } else {
            response = salesOrderService.getAll();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SalesOrder>> getById(@PathVariable String id) {
        ApiResponse<SalesOrder> response = salesOrderService.getById(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SalesOrder>> create(@RequestBody SalesOrder salesOrder) {
        return ResponseEntity.ok(salesOrderService.create(salesOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SalesOrder>> update(@PathVariable String id, @RequestBody SalesOrder salesOrder) {
        ApiResponse<SalesOrder> response = salesOrderService.update(id, salesOrder);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<SalesOrder>> changeStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        OrderStatus status = OrderStatus.valueOf(body.get("status"));
        ApiResponse<SalesOrder> response = salesOrderService.changeStatus(id, status);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/{id}/payments")
    public ResponseEntity<ApiResponse<List<Payment>>> getPayments(@PathVariable String id) {
        return ResponseEntity.ok(salesOrderService.getPayments(id));
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<ApiResponse<Payment>> addPayment(@PathVariable String id, @RequestBody Payment payment) {
        ApiResponse<Payment> response = salesOrderService.addPayment(id, payment);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
