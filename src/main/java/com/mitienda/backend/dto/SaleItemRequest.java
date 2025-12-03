package com.mitienda.backend.dto;

import lombok.Data;

@Data
public class SaleItemRequest {
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
}
