package com.minhthang.management.repository;

import com.minhthang.management.entity.ApprovalRequest;
import com.minhthang.management.entity.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, String> {
    List<ApprovalRequest> findByStatus(ApprovalStatus status);
    List<ApprovalRequest> findByRequestedById(String userId);
    long countByStatus(ApprovalStatus status);
}
