package com.mitienda.backend.service.impl;

import com.mitienda.backend.dto.SaleItemRequest;
import com.mitienda.backend.dto.SaleRequest;
import com.mitienda.backend.entity.Sale;
import com.mitienda.backend.entity.SaleItem;
import com.mitienda.backend.entity.Product;
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

        // Venta en estado pendiente
        Sale sale = new Sale();
        sale.setUserId(r.getUserId());
        sale.setStatus("PENDING");

        List<SaleItem> items = new ArrayList<>();
        double totalAmount = 0;  // 🔥 calcular monto total

        for (SaleItemRequest itemReq : r.getItems()) {

            // Buscar producto real
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // 🔥 RESTAR STOCK
            int nuevoStock = product.getStock() - itemReq.getQuantity();
            product.setStock(Math.max(nuevoStock, 0)); // evita negativos
            productRepository.save(product);

            // Crear item de la venta
            SaleItem si = new SaleItem();
            si.setSale(sale);
            si.setProductId(itemReq.getProductId());
            si.setQuantity(itemReq.getQuantity());
            si.setUnitPrice(itemReq.getUnitPrice());

            double subtotal = itemReq.getQuantity() * itemReq.getUnitPrice();
            si.setSubtotal(subtotal);

            totalAmount += subtotal; // acumular total

            items.add(si);
        }

        sale.setItems(items);
        sale.setAmount(totalAmount); // 🔥 guardar monto total en tabla sales

        return saleRepository.save(sale);
    }

}
