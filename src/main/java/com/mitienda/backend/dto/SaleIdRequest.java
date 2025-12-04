package com.mitienda.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SaleIdRequest {

    @Schema(description = "ID de la venta que se enviará a Webpay")
    private Long saleId;

}
