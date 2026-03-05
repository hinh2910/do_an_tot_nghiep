package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.Supplier;
import com.minhthang.management.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public ApiResponse<List<Supplier>> getAll() {
        return ApiResponse.success("Lấy danh sách nhà cung cấp thành công", supplierRepository.findAll());
    }

    public ApiResponse<List<Supplier>> search(String q) {
        return ApiResponse.success("Tìm kiếm nhà cung cấp thành công", supplierRepository.search(q));
    }

    public ApiResponse<Supplier> getById(String id) {
        return supplierRepository.findById(id)
                .map(s -> ApiResponse.success("Lấy nhà cung cấp thành công", s))
                .orElse(ApiResponse.error("Không tìm thấy nhà cung cấp"));
    }

    @Transactional
    public ApiResponse<Supplier> create(Supplier supplier) {
        Supplier saved = supplierRepository.save(supplier);
        return ApiResponse.success("Tạo nhà cung cấp thành công", saved);
    }

    @Transactional
    public ApiResponse<Supplier> update(String id, Supplier supplier) {
        return supplierRepository.findById(id)
                .map(existing -> {
                    existing.setName(supplier.getName());
                    existing.setCode(supplier.getCode());
                    existing.setContactPerson(supplier.getContactPerson());
                    existing.setPhone(supplier.getPhone());
                    existing.setEmail(supplier.getEmail());
                    existing.setAddress(supplier.getAddress());
                    return ApiResponse.success("Cập nhật nhà cung cấp thành công", supplierRepository.save(existing));
                })
                .orElse(ApiResponse.error("Không tìm thấy nhà cung cấp"));
    }

    @Transactional
    public ApiResponse<Supplier> toggleActive(String id) {
        return supplierRepository.findById(id)
                .map(supplier -> {
                    supplier.setActive(!supplier.isActive());
                    return ApiResponse.success(
                            supplier.isActive() ? "Kích hoạt nhà cung cấp thành công" : "Vô hiệu hóa nhà cung cấp thành công",
                            supplierRepository.save(supplier));
                })
                .orElse(ApiResponse.error("Không tìm thấy nhà cung cấp"));
    }

    @Transactional
    public ApiResponse<Void> delete(String id) {
        if (!supplierRepository.existsById(id)) {
            return ApiResponse.error("Không tìm thấy nhà cung cấp");
        }
        supplierRepository.deleteById(id);
        return ApiResponse.success("Xóa nhà cung cấp thành công");
    }
}
