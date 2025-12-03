package com.mitienda.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String buyOrder;
    private String token;
    private String authorizationCode;
    private Double amount;

    private String paymentTypeCode;
    private Integer responseCode;
    private String cardLast4;
    private String status;

    private LocalDateTime transactionDate;
    private LocalDateTime createdAt = LocalDateTime.now();

   @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items = new ArrayList<>();

}
