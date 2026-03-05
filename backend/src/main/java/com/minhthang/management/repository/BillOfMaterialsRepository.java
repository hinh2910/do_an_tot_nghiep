package com.minhthang.management.repository;

import com.minhthang.management.entity.BillOfMaterials;
import com.minhthang.management.entity.BomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BillOfMaterialsRepository extends JpaRepository<BillOfMaterials, String> {
    List<BillOfMaterials> findByProductId(String productId);
    List<BillOfMaterials> findByStatus(BomStatus status);
}
