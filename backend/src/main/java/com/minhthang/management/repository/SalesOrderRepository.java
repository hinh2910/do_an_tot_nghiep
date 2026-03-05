package com.minhthang.management.repository;

import com.minhthang.management.entity.OrderStatus;
import com.minhthang.management.entity.PaymentStatus;
import com.minhthang.management.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, String> {
    List<SalesOrder> findByStatus(OrderStatus status);
    List<SalesOrder> findByCustomerId(String customerId);
    List<SalesOrder> findBySalesStaffId(String salesStaffId);
    List<SalesOrder> findByPaymentStatus(PaymentStatus paymentStatus);
    long countByStatus(OrderStatus status);
}
