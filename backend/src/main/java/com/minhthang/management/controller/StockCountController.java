package com.minhthang.management.controller;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.StockCount;
import com.minhthang.management.service.StockCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-counts")
@RequiredArgsConstructor
public class StockCountController {

    private final StockCountService stockCountService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StockCount>>> getAll() {
        return ResponseEntity.ok(stockCountService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StockCount>> getById(@PathVariable String id) {
        ApiResponse<StockCount> response = stockCountService.getById(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StockCount>> create(@RequestBody StockCount stockCount) {
        return ResponseEntity.ok(stockCountService.create(stockCount));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StockCount>> update(@PathVariable String id, @RequestBody StockCount stockCount) {
        ApiResponse<StockCount> response = stockCountService.update(id, stockCount);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<StockCount>> complete(@PathVariable String id) {
        ApiResponse<StockCount> response = stockCountService.complete(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
