package com.minhthang.management.controller;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.RawMaterial;
import com.minhthang.management.entity.StockTransaction;
import com.minhthang.management.entity.TransactionType;
import com.minhthang.management.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<List<StockTransaction>>> getTransactions(
            @RequestParam(required = false) String warehouseId,
            @RequestParam(required = false) String type) {
        TransactionType transactionType = (type != null && !type.isEmpty()) ? TransactionType.valueOf(type) : null;
        return ResponseEntity.ok(stockService.getTransactions(warehouseId, transactionType));
    }

    @PostMapping("/in")
    public ResponseEntity<ApiResponse<StockTransaction>> stockIn(@RequestBody StockTransaction transaction) {
        ApiResponse<StockTransaction> response = stockService.stockIn(transaction);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/out")
    public ResponseEntity<ApiResponse<StockTransaction>> stockOut(@RequestBody StockTransaction transaction) {
        ApiResponse<StockTransaction> response = stockService.stockOut(transaction);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/adjustment")
    public ResponseEntity<ApiResponse<StockTransaction>> adjustment(@RequestBody StockTransaction transaction) {
        ApiResponse<StockTransaction> response = stockService.adjustment(transaction);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/inventory")
    public ResponseEntity<ApiResponse<List<RawMaterial>>> getInventory(@RequestParam(required = false) String warehouseId) {
        return ResponseEntity.ok(stockService.getInventory(warehouseId));
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<ApiResponse<RawMaterial>> getInventoryById(@PathVariable String id) {
        ApiResponse<RawMaterial> response = stockService.getInventoryById(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
