package com.minhthang.management.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock_counts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StockCount {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "count_number", unique = true)
    private String countNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "count_date")
    private LocalDate countDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StockCountStatus status = StockCountStatus.DRAFT;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "stockCount", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<StockCountItem> items = new ArrayList<>();

    @CreationTimestamp @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
