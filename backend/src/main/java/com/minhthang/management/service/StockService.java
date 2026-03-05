package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.RawMaterial;
import com.minhthang.management.entity.StockTransaction;
import com.minhthang.management.entity.TransactionType;
import com.minhthang.management.repository.RawMaterialRepository;
import com.minhthang.management.repository.StockTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockTransactionRepository stockTransactionRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public ApiResponse<List<StockTransaction>> getTransactions(String warehouseId, TransactionType type) {
        List<StockTransaction> transactions;
        if (warehouseId != null && type != null) {
            transactions = stockTransactionRepository.findByWarehouseIdAndType(warehouseId, type);
        } else if (warehouseId != null) {
            transactions = stockTransactionRepository.findByWarehouseId(warehouseId);
        } else if (type != null) {
            transactions = stockTransactionRepository.findByType(type);
        } else {
            transactions = stockTransactionRepository.findAll();
        }
        return ApiResponse.success("Lấy danh sách giao dịch kho thành công", transactions);
    }

    @Transactional
    public ApiResponse<StockTransaction> stockIn(StockTransaction transaction) {
        transaction.setTransactionNumber("TX-" + System.currentTimeMillis());
        transaction.setType(TransactionType.STOCK_IN);

        if (transaction.getRawMaterial() != null && transaction.getRawMaterial().getId() != null) {
            RawMaterial material = rawMaterialRepository.findById(transaction.getRawMaterial().getId()).orElse(null);
            if (material != null) {
                transaction.setPreviousQuantity(material.getQuantity());
                material.setQuantity(material.getQuantity().add(transaction.getQuantity()));
                rawMaterialRepository.save(material);
            }
        }

        StockTransaction saved = stockTransactionRepository.save(transaction);
        return ApiResponse.success("Nhập kho thành công", saved);
    }

    @Transactional
    public ApiResponse<StockTransaction> stockOut(StockTransaction transaction) {
        if (transaction.getRawMaterial() != null && transaction.getRawMaterial().getId() != null) {
            RawMaterial material = rawMaterialRepository.findById(transaction.getRawMaterial().getId()).orElse(null);
            if (material == null) {
                return ApiResponse.error("Không tìm thấy nguyên vật liệu");
            }
            if (material.getQuantity().compareTo(transaction.getQuantity()) < 0) {
                return ApiResponse.error("Số lượng tồn kho không đủ. Hiện có: " + material.getQuantity());
            }

            transaction.setTransactionNumber("TX-" + System.currentTimeMillis());
            transaction.setType(TransactionType.STOCK_OUT);
            transaction.setPreviousQuantity(material.getQuantity());
            material.setQuantity(material.getQuantity().subtract(transaction.getQuantity()));
            rawMaterialRepository.save(material);
        } else {
            transaction.setTransactionNumber("TX-" + System.currentTimeMillis());
            transaction.setType(TransactionType.STOCK_OUT);
        }

        StockTransaction saved = stockTransactionRepository.save(transaction);
        return ApiResponse.success("Xuất kho thành công", saved);
    }

    @Transactional
    public ApiResponse<StockTransaction> adjustment(StockTransaction transaction) {
        transaction.setTransactionNumber("TX-" + System.currentTimeMillis());
        transaction.setType(TransactionType.ADJUSTMENT);

        if (transaction.getRawMaterial() != null && transaction.getRawMaterial().getId() != null) {
            RawMaterial material = rawMaterialRepository.findById(transaction.getRawMaterial().getId()).orElse(null);
            if (material != null) {
                transaction.setPreviousQuantity(material.getQuantity());
                material.setQuantity(material.getQuantity().add(transaction.getQuantity()));
                rawMaterialRepository.save(material);
            }
        }

        StockTransaction saved = stockTransactionRepository.save(transaction);
        return ApiResponse.success("Điều chỉnh kho thành công", saved);
    }

    public ApiResponse<List<RawMaterial>> getInventory(String warehouseId) {
        List<RawMaterial> materials;
        if (warehouseId != null) {
            materials = rawMaterialRepository.findByWarehouseId(warehouseId);
        } else {
            materials = rawMaterialRepository.findAll();
        }
        return ApiResponse.success("Lấy tồn kho thành công", materials);
    }

    public ApiResponse<RawMaterial> getInventoryById(String materialId) {
        return rawMaterialRepository.findById(materialId)
                .map(m -> ApiResponse.success("Lấy thông tin tồn kho thành công", m))
                .orElse(ApiResponse.error("Không tìm thấy nguyên vật liệu"));
    }
}
