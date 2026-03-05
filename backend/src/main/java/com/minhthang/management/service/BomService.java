package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.BillOfMaterials;
import com.minhthang.management.entity.BomItem;
import com.minhthang.management.entity.BomStatus;
import com.minhthang.management.repository.BillOfMaterialsRepository;
import com.minhthang.management.repository.BomItemRepository;
import com.minhthang.management.repository.ProductRepository;
import com.minhthang.management.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BomService {

    private final BillOfMaterialsRepository bomRepository;
    private final BomItemRepository bomItemRepository;
    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public ApiResponse<List<BillOfMaterials>> getAll() {
        return ApiResponse.success("Lấy danh sách BOM thành công", bomRepository.findAll());
    }

    public ApiResponse<BillOfMaterials> getById(String id) {
        return bomRepository.findById(id)
                .map(bom -> ApiResponse.success("Lấy BOM thành công", bom))
                .orElse(ApiResponse.error("Không tìm thấy BOM"));
    }

    @Transactional
    public ApiResponse<BillOfMaterials> create(BillOfMaterials bom) {
        BillOfMaterials saved = bomRepository.save(bom);
        return ApiResponse.success("Tạo BOM thành công", saved);
    }

    @Transactional
    public ApiResponse<BillOfMaterials> update(String id, BillOfMaterials bom) {
        return bomRepository.findById(id)
                .map(existing -> {
                    existing.setName(bom.getName());
                    existing.setDescription(bom.getDescription());
                    existing.setProduct(bom.getProduct());
                    existing.setVersion(bom.getVersion());
                    return ApiResponse.success("Cập nhật BOM thành công", bomRepository.save(existing));
                })
                .orElse(ApiResponse.error("Không tìm thấy BOM"));
    }

    @Transactional
    public ApiResponse<BillOfMaterials> changeStatus(String id, BomStatus status) {
        return bomRepository.findById(id)
                .map(bom -> {
                    bom.setStatus(status);
                    return ApiResponse.success("Cập nhật trạng thái BOM thành công", bomRepository.save(bom));
                })
                .orElse(ApiResponse.error("Không tìm thấy BOM"));
    }

    @Transactional
    public ApiResponse<BomItem> addItem(String bomId, BomItem item) {
        return bomRepository.findById(bomId)
                .map(bom -> {
                    item.setBom(bom);
                    BomItem saved = bomItemRepository.save(item);
                    return ApiResponse.success("Thêm item vào BOM thành công", saved);
                })
                .orElse(ApiResponse.error("Không tìm thấy BOM"));
    }

    @Transactional
    public ApiResponse<Void> removeItem(String itemId) {
        if (!bomItemRepository.existsById(itemId)) {
            return ApiResponse.error("Không tìm thấy BOM item");
        }
        bomItemRepository.deleteById(itemId);
        return ApiResponse.success("Xóa BOM item thành công");
    }

    public ApiResponse<List<BillOfMaterials>> findByProductId(String productId) {
        if (!productRepository.existsById(productId)) {
            return ApiResponse.error("Không tìm thấy sản phẩm");
        }
        return ApiResponse.success("Lấy BOM theo sản phẩm thành công",
                bomRepository.findByProductId(productId));
    }
}
