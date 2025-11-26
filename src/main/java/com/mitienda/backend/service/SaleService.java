package com.mitienda.backend.service;

import com.mitienda.backend.entity.Sale;
import com.mitienda.backend.dto.SaleRequest;

public interface SaleService {
    Sale createSale(SaleRequest request);
}
