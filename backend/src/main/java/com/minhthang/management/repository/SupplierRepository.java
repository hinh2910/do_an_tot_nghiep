package com.minhthang.management.repository;

import com.minhthang.management.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, String> {
    List<Supplier> findByActiveTrue();
    @Query("SELECT s FROM Supplier s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(s.code) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Supplier> search(String q);
    long countByActiveTrue();
}
