package com.minhthang.management.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductionOrder {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "order_number", unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sales_order_id")
    private SalesOrder salesOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(precision = 15, scale = 2)
    private BigDecimal quantity;

    @Builder.Default
    @Column(name = "completed_quantity", precision = 15, scale = 2)
    private BigDecimal completedQuantity = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "scrap_quantity", precision = 15, scale = 2)
    private BigDecimal scrapQuantity = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProductionStatus status = ProductionStatus.PLANNED;

    @Builder.Default
    private int priority = 0;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bom_id")
    private BillOfMaterials bom;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
