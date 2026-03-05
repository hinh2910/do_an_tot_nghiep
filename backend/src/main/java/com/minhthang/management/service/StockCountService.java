package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.StockCount;
import com.minhthang.management.entity.StockCountStatus;
import com.minhthang.management.repository.StockCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockCountService {

    private final StockCountRepository stockCountRepository;

    public ApiResponse<List<StockCount>> getAll() {
        return ApiResponse.success("Lấy danh sách kiểm kê thành công", stockCountRepository.findAll());
    }

    public ApiResponse<StockCount> getById(String id) {
        return stockCountRepository.findById(id)
                .map(sc -> ApiResponse.success("Lấy thông tin kiểm kê thành công", sc))
                .orElse(ApiResponse.error("Không tìm thấy phiếu kiểm kê"));
    }

    @Transactional
    public ApiResponse<StockCount> create(StockCount stockCount) {
        stockCount.setCountNumber("SC-" + System.currentTimeMillis());

        if (stockCount.getItems() != null) {
            stockCount.getItems().forEach(item -> item.setStockCount(stockCount));
        }

        StockCount saved = stockCountRepository.save(stockCount);
        return ApiResponse.success("Tạo phiếu kiểm kê thành công", saved);
    }

    @Transactional
    public ApiResponse<StockCount> update(String id, StockCount stockCount) {
        return stockCountRepository.findById(id).map(existing -> {
            existing.setWarehouse(stockCount.getWarehouse());
            existing.setCountDate(stockCount.getCountDate());
            existing.setNotes(stockCount.getNotes());
            existing.setCreatedBy(stockCount.getCreatedBy());

            existing.getItems().clear();
            if (stockCount.getItems() != null) {
                stockCount.getItems().forEach(item -> {
                    item.setStockCount(existing);
                    existing.getItems().add(item);
                });
            }

            return ApiResponse.success("Cập nhật phiếu kiểm kê thành công", stockCountRepository.save(existing));
        }).orElse(ApiResponse.error("Không tìm thấy phiếu kiểm kê"));
    }

    @Transactional
    public ApiResponse<StockCount> complete(String id) {
        return stockCountRepository.findById(id).map(existing -> {
            existing.setStatus(StockCountStatus.COMPLETED);

            if (existing.getItems() != null) {
                existing.getItems().forEach(item -> {
                    if (item.getActualQuantity() != null && item.getSystemQuantity() != null) {
                        item.setDifference(item.getActualQuantity().subtract(item.getSystemQuantity()));
                    }
                });
            }

            return ApiResponse.success("Hoàn thành kiểm kê thành công", stockCountRepository.save(existing));
        }).orElse(ApiResponse.error("Không tìm thấy phiếu kiểm kê"));
    }
}
