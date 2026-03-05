package com.minhthang.management.repository;

import com.minhthang.management.entity.WorkOrder;
import com.minhthang.management.entity.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, String> {
    List<WorkOrder> findByProductionOrderId(String productionOrderId);
    List<WorkOrder> findByStatus(WorkOrderStatus status);
    List<WorkOrder> findByAssignedToId(String assignedToId);
}
