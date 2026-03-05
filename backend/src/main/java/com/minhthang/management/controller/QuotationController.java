package com.minhthang.management.controller;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.Quotation;
import com.minhthang.management.entity.QuotationStatus;
import com.minhthang.management.service.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quotations")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Quotation>>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String customerId) {
        ApiResponse<List<Quotation>> response;
        if (status != null && !status.isEmpty()) {
            response = quotationService.getByStatus(QuotationStatus.valueOf(status));
        } else if (customerId != null && !customerId.isEmpty()) {
            response = quotationService.getByCustomerId(customerId);
        } else {
            response = quotationService.getAll();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Quotation>> getById(@PathVariable String id) {
        ApiResponse<Quotation> response = quotationService.getById(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Quotation>> create(@RequestBody Quotation quotation) {
        return ResponseEntity.ok(quotationService.create(quotation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Quotation>> update(@PathVariable String id, @RequestBody Quotation quotation) {
        ApiResponse<Quotation> response = quotationService.update(id, quotation);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Quotation>> changeStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        QuotationStatus status = QuotationStatus.valueOf(body.get("status"));
        ApiResponse<Quotation> response = quotationService.changeStatus(id, status);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/history/{customerId}")
    public ResponseEntity<ApiResponse<List<Quotation>>> getByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(quotationService.getByCustomerId(customerId));
    }
}
