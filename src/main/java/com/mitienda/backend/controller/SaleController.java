package com.mitienda.backend.controller;

import com.mitienda.backend.dto.SaleRequest;
import com.mitienda.backend.entity.Sale;
import com.mitienda.backend.service.SaleService;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales")
@CrossOrigin("*")
public class SaleController {

    private final SaleService service;

    public SaleController(SaleService service) {
        this.service = service;
    }

    // 🔹 Crear venta
    @PostMapping
    public ResponseEntity<?> createSale(@RequestBody SaleRequest request) {
        Sale sale = service.createSale(request);

        return ResponseEntity.ok(Map.of(
                "id", sale.getId(),
                "total", sale.getTotal(),
                "estado", sale.getEstado(),
                "fecha", sale.getFecha()
        ));
    }

    // 🔹 Obtener TODAS las ventas (ADMIN)
    @GetMapping
    public ResponseEntity<?> getAllSales() {
        return ResponseEntity.ok(service.getAllSales());
    }
}
