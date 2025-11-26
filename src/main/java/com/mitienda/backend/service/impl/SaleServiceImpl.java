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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepo;
    private final ProductRepository productRepo;

    public SaleServiceImpl(SaleRepository saleRepo, ProductRepository productRepo) {
        this.saleRepo = saleRepo;
        this.productRepo = productRepo;
    }

    @Override
    public Sale createSale(SaleRequest request) {

        double total = 0.0;
        List<SaleItem> saleItems = new ArrayList<>();

        // 1) VALIDAR STOCK, CALCULAR TOTAL, DESCONTAR STOCK
        for (SaleItemRequest itemReq : request.getItems()) {

            Product product = productRepo.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (product.getStock() < itemReq.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + product.getNombre());
            }

            // Precio total del producto
            double subtotal = product.getPrecio() * itemReq.getCantidad();
            total += subtotal;

            // Descontar stock
            product.setStock(product.getStock() - itemReq.getCantidad());
            productRepo.save(product);

            // Crear SaleItem
            SaleItem saleItem = new SaleItem();
            saleItem.setProductId(product.getId());
            saleItem.setCantidad(itemReq.getCantidad());
            saleItem.setPrecioUnitario(product.getPrecio());
            saleItems.add(saleItem);
        }

        // 2) CALCULAR IVA
        double iva = total * 0.19;

        // 3) CREAR OBJETO SALE
        Sale sale = new Sale();
        sale.setUserId(request.getUserId());
        sale.setItems(saleItems);
        sale.setTotal(total);
        sale.setIva(iva);
        sale.setFecha(LocalDateTime.now());

        // Estado simulado (por ahora siempre aprobado)
        sale.setEstado("APROBADO");

        // 4) Asignar relación bidireccional
        for (SaleItem si : saleItems) {
            si.setSale(sale);
        }

        // 5) Guardar venta en BD
        return saleRepo.save(sale);
    }
}
