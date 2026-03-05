package com.minhthang.management.controller;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.ApprovalRequest;
import com.minhthang.management.entity.ApprovalStatus;
import com.minhthang.management.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApprovalRequest>>> getAll(
            @RequestParam(required = false) String status
    ) {
        if (status != null && !status.isBlank()) {
            ApprovalStatus approvalStatus = ApprovalStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(approvalService.getByStatus(approvalStatus));
        }
        return ResponseEntity.ok(approvalService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApprovalRequest>> getById(@PathVariable String id) {
        ApiResponse<ApprovalRequest> response = approvalService.getById(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApprovalRequest>> create(@RequestBody ApprovalRequest request) {
        return ResponseEntity.ok(approvalService.create(request));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<ApprovalRequest>> approve(
            @PathVariable String id,
            @RequestBody Map<String, String> body
    ) {
        String userId = body.get("userId");
        String notes = body.get("notes");
        ApiResponse<ApprovalRequest> response = approvalService.approve(id, userId, notes);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<ApprovalRequest>> reject(
            @PathVariable String id,
            @RequestBody Map<String, String> body
    ) {
        String userId = body.get("userId");
        String notes = body.get("notes");
        ApiResponse<ApprovalRequest> response = approvalService.reject(id, userId, notes);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
