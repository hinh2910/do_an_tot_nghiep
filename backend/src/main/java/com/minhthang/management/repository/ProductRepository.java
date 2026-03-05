package com.minhthang.management.repository;

import com.minhthang.management.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByActiveTrue();
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(p.sku) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Product> search(String q);
    List<Product> findByCategoryId(String categoryId);
    boolean existsBySku(String sku);
    long countByActiveTrue();
}
