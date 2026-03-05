package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.WorkOrder;
import com.minhthang.management.entity.WorkOrderStatus;
import com.minhthang.management.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;

    public ApiResponse<List<WorkOrder>> getAll() {
        return ApiResponse.success("Lấy danh sách công việc thành công", workOrderRepository.findAll());
    }

    public ApiResponse<List<WorkOrder>> getByProductionOrderId(String productionOrderId) {
        return ApiResponse.success("Lấy danh sách công việc thành công", workOrderRepository.findByProductionOrderId(productionOrderId));
    }

    public ApiResponse<List<WorkOrder>> getByStatus(WorkOrderStatus status) {
        return ApiResponse.success("Lấy danh sách công việc thành công", workOrderRepository.findByStatus(status));
    }

    public ApiResponse<WorkOrder> getById(String id) {
        return workOrderRepository.findById(id)
                .map(w -> ApiResponse.success("Lấy thông tin công việc thành công", w))
                .orElse(ApiResponse.error("Không tìm thấy công việc"));
    }

    @Transactional
    public ApiResponse<WorkOrder> create(WorkOrder workOrder) {
        workOrder.setWorkOrderNumber("WO-" + System.currentTimeMillis());
        WorkOrder saved = workOrderRepository.save(workOrder);
        return ApiResponse.success("Tạo công việc thành công", saved);
    }

    @Transactional
    public ApiResponse<WorkOrder> update(String id, WorkOrder workOrder) {
        return workOrderRepository.findById(id).map(existing -> {
            existing.setProductionOrder(workOrder.getProductionOrder());
            existing.setDescription(workOrder.getDescription());
            existing.setAssignedTo(workOrder.getAssignedTo());
            existing.setStartDate(workOrder.getStartDate());
            existing.setEndDate(workOrder.getEndDate());
            existing.setNotes(workOrder.getNotes());
            return ApiResponse.success("Cập nhật công việc thành công", workOrderRepository.save(existing));
        }).orElse(ApiResponse.error("Không tìm thấy công việc"));
    }

    @Transactional
    public ApiResponse<WorkOrder> changeStatus(String id, WorkOrderStatus status) {
        return workOrderRepository.findById(id).map(existing -> {
            existing.setStatus(status);
            return ApiResponse.success("Cập nhật trạng thái công việc thành công", workOrderRepository.save(existing));
        }).orElse(ApiResponse.error("Không tìm thấy công việc"));
    }

    @Transactional
    public ApiResponse<WorkOrder> updateProgress(String id, BigDecimal completedQuantity, BigDecimal scrapQuantity) {
        return workOrderRepository.findById(id).map(existing -> {
            existing.setCompletedQuantity(completedQuantity);
            existing.setScrapQuantity(scrapQuantity);
            return ApiResponse.success("Cập nhật tiến độ công việc thành công", workOrderRepository.save(existing));
        }).orElse(ApiResponse.error("Không tìm thấy công việc"));
    }
}
