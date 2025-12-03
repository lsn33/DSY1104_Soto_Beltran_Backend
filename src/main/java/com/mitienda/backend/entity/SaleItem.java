package com.mitienda.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sale_items")
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id") // ← columna real en la BD
    private Sale sale;

    @Column(name = "product_id") // ← columna real en la BD
    private Long productId;

    private Integer quantity;
    private Double subtotal;

    @Column(name = "unit_price") // ← columna real en la BD
    private Double unitPrice;
}
