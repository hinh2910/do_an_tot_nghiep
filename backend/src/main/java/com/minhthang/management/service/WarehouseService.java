package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.Warehouse;
import com.minhthang.management.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public ApiResponse<List<Warehouse>> getAll() {
        return ApiResponse.success("Lấy danh sách kho thành công", warehouseRepository.findAll());
    }

    public ApiResponse<List<Warehouse>> search(String q) {
        return ApiResponse.success("Tìm kiếm kho thành công", warehouseRepository.search(q));
    }

    public ApiResponse<Warehouse> getById(String id) {
        return warehouseRepository.findById(id)
                .map(w -> ApiResponse.success("Lấy thông tin kho thành công", w))
                .orElse(ApiResponse.error("Không tìm thấy kho"));
    }

    @Transactional
    public ApiResponse<Warehouse> create(Warehouse warehouse) {
        Warehouse saved = warehouseRepository.save(warehouse);
        return ApiResponse.success("Tạo kho thành công", saved);
    }

    @Transactional
    public ApiResponse<Warehouse> update(String id, Warehouse warehouse) {
        return warehouseRepository.findById(id)
                .map(existing -> {
                    existing.setName(warehouse.getName());
                    existing.setCode(warehouse.getCode());
                    existing.setLocation(warehouse.getLocation());
                    existing.setAddress(warehouse.getAddress());
                    existing.setCapacity(warehouse.getCapacity());
                    return ApiResponse.success("Cập nhật kho thành công", warehouseRepository.save(existing));
                })
                .orElse(ApiResponse.error("Không tìm thấy kho"));
    }

    @Transactional
    public ApiResponse<Warehouse> toggleActive(String id) {
        return warehouseRepository.findById(id)
                .map(warehouse -> {
                    warehouse.setActive(!warehouse.isActive());
                    return ApiResponse.success(
                            warehouse.isActive() ? "Kích hoạt kho thành công" : "Vô hiệu hóa kho thành công",
                            warehouseRepository.save(warehouse));
                })
                .orElse(ApiResponse.error("Không tìm thấy kho"));
    }
}
