package com.mitienda.backend.service;

import com.mitienda.backend.entity.Sale;
import com.mitienda.backend.dto.SaleRequest;

import java.util.List;

public interface SaleService {

    Sale createSale(SaleRequest request);

    // 🔥 Nuevo método para listar todas las ventas (usado por AdminOrdersPage)
    List<Sale> getAllSales();
}
