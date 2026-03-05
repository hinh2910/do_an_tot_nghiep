package com.minhthang.management.controller;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.Warehouse;
import com.minhthang.management.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Warehouse>>> getAll(
            @RequestParam(required = false) String q
    ) {
        ApiResponse<List<Warehouse>> response = (q != null && !q.isBlank())
                ? warehouseService.search(q)
                : warehouseService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Warehouse>> getById(@PathVariable String id) {
        ApiResponse<Warehouse> response = warehouseService.getById(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Warehouse>> create(@RequestBody Warehouse warehouse) {
        return ResponseEntity.ok(warehouseService.create(warehouse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Warehouse>> update(
            @PathVariable String id,
            @RequestBody Warehouse warehouse
    ) {
        ApiResponse<Warehouse> response = warehouseService.update(id, warehouse);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<Warehouse>> toggleActive(@PathVariable String id) {
        ApiResponse<Warehouse> response = warehouseService.toggleActive(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
