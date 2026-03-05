package com.minhthang.management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "stock_count_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StockCountItem {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_count_id")
    @JsonBackReference
    private StockCount stockCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "raw_material_id")
    private RawMaterial rawMaterial;

    @Column(name = "system_quantity", precision = 15, scale = 2)
    private BigDecimal systemQuantity;

    @Column(name = "actual_quantity", precision = 15, scale = 2)
    private BigDecimal actualQuantity;

    @Column(precision = 15, scale = 2)
    private BigDecimal difference;

    private String notes;
}
