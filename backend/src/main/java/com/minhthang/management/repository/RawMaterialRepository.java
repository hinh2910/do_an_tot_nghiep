package com.minhthang.management.repository;

import com.minhthang.management.entity.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RawMaterialRepository extends JpaRepository<RawMaterial, String> {
    List<RawMaterial> findByActiveTrue();
    @Query("SELECT r FROM RawMaterial r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(r.sku) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<RawMaterial> search(String q);
    List<RawMaterial> findBySupplierId(String supplierId);
    List<RawMaterial> findByWarehouseId(String warehouseId);
    long countByActiveTrue();
}
