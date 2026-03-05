package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.RawMaterial;
import com.minhthang.management.repository.RawMaterialRepository;
import com.minhthang.management.repository.SupplierRepository;
import com.minhthang.management.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;
    private final SupplierRepository supplierRepository;
    private final WarehouseRepository warehouseRepository;

    public ApiResponse<List<RawMaterial>> getAll() {
        return ApiResponse.success("Lấy danh sách nguyên vật liệu thành công", rawMaterialRepository.findAll());
    }

    public ApiResponse<List<RawMaterial>> search(String q) {
        return ApiResponse.success("Tìm kiếm nguyên vật liệu thành công", rawMaterialRepository.search(q));
    }

    public ApiResponse<RawMaterial> getById(String id) {
        return rawMaterialRepository.findById(id)
                .map(m -> ApiResponse.success("Lấy nguyên vật liệu thành công", m))
                .orElse(ApiResponse.error("Không tìm thấy nguyên vật liệu"));
    }

    @Transactional
    public ApiResponse<RawMaterial> create(RawMaterial material) {
        RawMaterial saved = rawMaterialRepository.save(material);
        return ApiResponse.success("Tạo nguyên vật liệu thành công", saved);
    }

    @Transactional
    public ApiResponse<RawMaterial> update(String id, RawMaterial material) {
        return rawMaterialRepository.findById(id)
                .map(existing -> {
                    existing.setName(material.getName());
                    existing.setSku(material.getSku());
                    existing.setDescription(material.getDescription());
                    existing.setUnit(material.getUnit());
                    existing.setSupplier(material.getSupplier());
                    existing.setWarehouse(material.getWarehouse());
                    existing.setQuantity(material.getQuantity());
                    existing.setMinQuantity(material.getMinQuantity());
                    existing.setPrice(material.getPrice());
                    return ApiResponse.success("Cập nhật nguyên vật liệu thành công", rawMaterialRepository.save(existing));
                })
                .orElse(ApiResponse.error("Không tìm thấy nguyên vật liệu"));
    }

    @Transactional
    public ApiResponse<RawMaterial> toggleActive(String id) {
        return rawMaterialRepository.findById(id)
                .map(material -> {
                    material.setActive(!material.isActive());
                    return ApiResponse.success(
                            material.isActive() ? "Kích hoạt nguyên vật liệu thành công" : "Vô hiệu hóa nguyên vật liệu thành công",
                            rawMaterialRepository.save(material));
                })
                .orElse(ApiResponse.error("Không tìm thấy nguyên vật liệu"));
    }

    public ApiResponse<List<RawMaterial>> findBySupplierId(String supplierId) {
        if (!supplierRepository.existsById(supplierId)) {
            return ApiResponse.error("Không tìm thấy nhà cung cấp");
        }
        return ApiResponse.success("Lấy nguyên vật liệu theo nhà cung cấp thành công",
                rawMaterialRepository.findBySupplierId(supplierId));
    }

    public ApiResponse<List<RawMaterial>> findByWarehouseId(String warehouseId) {
        if (!warehouseRepository.existsById(warehouseId)) {
            return ApiResponse.error("Không tìm thấy kho");
        }
        return ApiResponse.success("Lấy nguyên vật liệu theo kho thành công",
                rawMaterialRepository.findByWarehouseId(warehouseId));
    }
}
