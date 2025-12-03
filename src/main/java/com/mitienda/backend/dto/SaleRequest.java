package com.mitienda.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaleRequest {
    private Long userId;
    private List<SaleItemRequest> items;
}
