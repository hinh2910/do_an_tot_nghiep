package com.minhthang.management.repository;

import com.minhthang.management.entity.ProductionOrder;
import com.minhthang.management.entity.ProductionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, String> {
    List<ProductionOrder> findByStatus(ProductionStatus status);
    List<ProductionOrder> findBySalesOrderId(String salesOrderId);
    long countByStatus(ProductionStatus status);
}
