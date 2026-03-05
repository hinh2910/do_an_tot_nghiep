package com.minhthang.management.controller;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.RawMaterial;
import com.minhthang.management.service.RawMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RawMaterial>>> getAll(
            @RequestParam(required = false) String q
    ) {
        ApiResponse<List<RawMaterial>> response = (q != null && !q.isBlank())
                ? rawMaterialService.search(q)
                : rawMaterialService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RawMaterial>> getById(@PathVariable String id) {
        ApiResponse<RawMaterial> response = rawMaterialService.getById(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RawMaterial>> create(@RequestBody RawMaterial material) {
        return ResponseEntity.ok(rawMaterialService.create(material));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RawMaterial>> update(
            @PathVariable String id,
            @RequestBody RawMaterial material
    ) {
        ApiResponse<RawMaterial> response = rawMaterialService.update(id, material);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<RawMaterial>> toggleActive(@PathVariable String id) {
        ApiResponse<RawMaterial> response = rawMaterialService.toggleActive(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/by-supplier/{supplierId}")
    public ResponseEntity<ApiResponse<List<RawMaterial>>> findBySupplierId(@PathVariable String supplierId) {
        ApiResponse<List<RawMaterial>> response = rawMaterialService.findBySupplierId(supplierId);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/by-warehouse/{warehouseId}")
    public ResponseEntity<ApiResponse<List<RawMaterial>>> findByWarehouseId(@PathVariable String warehouseId) {
        ApiResponse<List<RawMaterial>> response = rawMaterialService.findByWarehouseId(warehouseId);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
