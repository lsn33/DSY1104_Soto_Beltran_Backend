package com.mitienda.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JoinColumn(name = "sale_id")
    @JsonIgnore   // 👈 EVITA CICLO INFINITO
    private Sale sale;

    @Column(name = "product_id")
    private Long productId;


    private Integer cantidad;

    @Column(name = "precio_unitario")
    private Double precioUnitario;
}
