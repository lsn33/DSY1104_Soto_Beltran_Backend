package com.mitienda.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleRequest {
    
    private Long userId;
    private String buyOrder;
    private String token;
    private Double amount;
    private String authorizationCode;
    private LocalDateTime transactionDate;
    private String paymentTypeCode;
    private Integer responseCode;
    private String cardLast4;
    private String status;

    private List<SaleItemRequest> items;
}
