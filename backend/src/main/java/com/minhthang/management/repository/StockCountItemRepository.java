package com.minhthang.management.repository;

import com.minhthang.management.entity.StockCountItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockCountItemRepository extends JpaRepository<StockCountItem, String> {
}
