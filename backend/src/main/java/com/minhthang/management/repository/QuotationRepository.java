package com.minhthang.management.repository;

import com.minhthang.management.entity.Quotation;
import com.minhthang.management.entity.QuotationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, String> {
    List<Quotation> findByStatus(QuotationStatus status);
    List<Quotation> findByCustomerId(String customerId);
    List<Quotation> findBySalesStaffId(String salesStaffId);
    long countByStatus(QuotationStatus status);
}
