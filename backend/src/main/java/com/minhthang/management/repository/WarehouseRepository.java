package com.minhthang.management.repository;

import com.minhthang.management.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, String> {
    List<Warehouse> findByActiveTrue();
    @Query("SELECT w FROM Warehouse w WHERE LOWER(w.name) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(w.code) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Warehouse> search(String q);
}
