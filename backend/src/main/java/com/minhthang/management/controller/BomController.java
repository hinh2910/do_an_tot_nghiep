package com.minhthang.management.controller;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.BillOfMaterials;
import com.minhthang.management.entity.BomItem;
import com.minhthang.management.entity.BomStatus;
import com.minhthang.management.service.BomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boms")
@RequiredArgsConstructor
public class BomController {

    private final BomService bomService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BillOfMaterials>>> getAll() {
        return ResponseEntity.ok(bomService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BillOfMaterials>> getById(@PathVariable String id) {
        ApiResponse<BillOfMaterials> response = bomService.getById(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BillOfMaterials>> create(@RequestBody BillOfMaterials bom) {
        return ResponseEntity.ok(bomService.create(bom));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BillOfMaterials>> update(
            @PathVariable String id,
            @RequestBody BillOfMaterials bom
    ) {
        ApiResponse<BillOfMaterials> response = bomService.update(id, bom);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BillOfMaterials>> changeStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body
    ) {
        BomStatus status = BomStatus.valueOf(body.get("status"));
        ApiResponse<BillOfMaterials> response = bomService.changeStatus(id, status);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<ApiResponse<BomItem>> addItem(
            @PathVariable String id,
            @RequestBody BomItem item
    ) {
        ApiResponse<BomItem> response = bomService.addItem(id, item);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<Void>> removeItem(@PathVariable String itemId) {
        ApiResponse<Void> response = bomService.removeItem(itemId);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
