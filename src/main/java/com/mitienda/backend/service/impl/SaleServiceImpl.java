package com.mitienda.backend.service.impl;

import com.mitienda.backend.dto.SaleItemRequest;
import com.mitienda.backend.dto.SaleRequest;
import com.mitienda.backend.entity.Product;
import com.mitienda.backend.entity.Sale;
import com.mitienda.backend.entity.SaleItem;
import com.mitienda.backend.repository.ProductRepository;
import com.mitienda.backend.repository.SaleRepository;
import com.mitienda.backend.service.SaleService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    public SaleServiceImpl(SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Sale createSale(SaleRequest r) {

        // Crear venta
        Sale sale = new Sale();
        sale.setUserId(r.getUserId());
        sale.setBuyOrder(r.getBuyOrder());
        sale.setToken(r.getToken());
        sale.setAuthorizationCode(r.getAuthorizationCode());
        sale.setAmount(r.getAmount());
        sale.setPaymentTypeCode(r.getPaymentTypeCode());
        sale.setResponseCode(r.getResponseCode());
        sale.setCardLast4(r.getCardLast4());
        sale.setStatus(r.getStatus());
        sale.setTransactionDate(r.getTransactionDate());

        // Lista de ítems
        List<SaleItem> items = new ArrayList<>();

        for (SaleItemRequest itemReq : r.getItems()) {

            // Buscar producto real
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemReq.getProductId()));

            // Descontar stock
            int nuevoStock = product.getStock() - itemReq.getQuantity();
            if (nuevoStock < 0) nuevoStock = 0; // Evitar stock negativo

            product.setStock(nuevoStock);
            productRepository.save(product); // Guardar stock actualizado

            // Crear item de venta
            SaleItem si = new SaleItem();
            si.setSale(sale);
            si.setProductId(itemReq.getProductId());
            si.setQuantity(itemReq.getQuantity());
            si.setUnitPrice(itemReq.getUnitPrice());
            si.setSubtotal(itemReq.getQuantity() * itemReq.getUnitPrice());

            items.add(si);
        }

        // Asignar ítems a la venta
        sale.setItems(items);

        // Guardar venta + items
        return saleRepository.save(sale);
    }
}
