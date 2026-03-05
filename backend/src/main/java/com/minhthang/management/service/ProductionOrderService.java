package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.ProductionOrder;
import com.minhthang.management.entity.ProductionStatus;
import com.minhthang.management.repository.ProductionOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductionOrderService {

    private final ProductionOrderRepository productionOrderRepository;

    public ApiResponse<List<ProductionOrder>> getAll() {
        return ApiResponse.success("Lấy danh sách lệnh sản xuất thành công", productionOrderRepository.findAll());
    }

    public ApiResponse<List<ProductionOrder>> getByStatus(ProductionStatus status) {
        return ApiResponse.success("Lấy danh sách lệnh sản xuất thành công", productionOrderRepository.findByStatus(status));
    }

    public ApiResponse<ProductionOrder> getById(String id) {
        return productionOrderRepository.findById(id)
                .map(p -> ApiResponse.success("Lấy thông tin lệnh sản xuất thành công", p))
                .orElse(ApiResponse.error("Không tìm thấy lệnh sản xuất"));
    }

    @Transactional
    public ApiResponse<ProductionOrder> create(ProductionOrder productionOrder) {
        productionOrder.setOrderNumber("PO-" + System.currentTimeMillis());
        ProductionOrder saved = productionOrderRepository.save(productionOrder);
        return ApiResponse.success("Tạo lệnh sản xuất thành công", saved);
    }

    @Transactional
    public ApiResponse<ProductionOrder> update(String id, ProductionOrder productionOrder) {
        return productionOrderRepository.findById(id).map(existing -> {
            existing.setSalesOrder(productionOrder.getSalesOrder());
            existing.setProduct(productionOrder.getProduct());
            existing.setQuantity(productionOrder.getQuantity());
            existing.setPriority(productionOrder.getPriority());
            existing.setStartDate(productionOrder.getStartDate());
            existing.setEndDate(productionOrder.getEndDate());
            existing.setBom(productionOrder.getBom());
            existing.setNotes(productionOrder.getNotes());
            return ApiResponse.success("Cập nhật lệnh sản xuất thành công", productionOrderRepository.save(existing));
        }).orElse(ApiResponse.error("Không tìm thấy lệnh sản xuất"));
    }

    @Transactional
    public ApiResponse<ProductionOrder> changeStatus(String id, ProductionStatus status) {
        return productionOrderRepository.findById(id).map(existing -> {
            existing.setStatus(status);
            return ApiResponse.success("Cập nhật trạng thái lệnh sản xuất thành công", productionOrderRepository.save(existing));
        }).orElse(ApiResponse.error("Không tìm thấy lệnh sản xuất"));
    }

    @Transactional
    public ApiResponse<ProductionOrder> updateProgress(String id, BigDecimal completedQuantity, BigDecimal scrapQuantity) {
        return productionOrderRepository.findById(id).map(existing -> {
            existing.setCompletedQuantity(completedQuantity);
            existing.setScrapQuantity(scrapQuantity);
            return ApiResponse.success("Cập nhật tiến độ sản xuất thành công", productionOrderRepository.save(existing));
        }).orElse(ApiResponse.error("Không tìm thấy lệnh sản xuất"));
    }
}
