package com.minhthang.management.repository;

import com.minhthang.management.entity.BomItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomItemRepository extends JpaRepository<BomItem, String> {
}
