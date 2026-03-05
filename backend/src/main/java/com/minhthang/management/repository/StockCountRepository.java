package com.minhthang.management.repository;

import com.minhthang.management.entity.StockCount;
import com.minhthang.management.entity.StockCountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockCountRepository extends JpaRepository<StockCount, String> {
    List<StockCount> findByStatus(StockCountStatus status);
    List<StockCount> findByWarehouseId(String warehouseId);
}
