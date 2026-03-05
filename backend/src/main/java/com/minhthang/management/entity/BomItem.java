package com.minhthang.management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bom_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BomItem {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_id", nullable = false)
    @JsonBackReference
    private BillOfMaterials bom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterial rawMaterial;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantity;

    @Column(length = 30)
    private String unit;

    private String notes;
}
