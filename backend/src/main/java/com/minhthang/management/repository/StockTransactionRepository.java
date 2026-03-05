package com.minhthang.management.repository;

import com.minhthang.management.entity.StockTransaction;
import com.minhthang.management.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, String> {
    List<StockTransaction> findByType(TransactionType type);
    List<StockTransaction> findByWarehouseId(String warehouseId);
    List<StockTransaction> findByWarehouseIdAndType(String warehouseId, TransactionType type);
}
