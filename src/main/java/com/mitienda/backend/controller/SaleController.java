package com.mitienda.backend.controller;

import com.mitienda.backend.dto.SaleRequest;
import com.mitienda.backend.entity.Sale;
import com.mitienda.backend.service.SaleService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/sales")
@CrossOrigin("*")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    // =========================================================
    // 🔥 CREAR VENTA PENDING (ANTES DE TRANSBANK)
    // =========================================================
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SaleRequest request) {

        // Validación básica
        if (request.getUserId() == null || request.getItems() == null || request.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Datos inválidos: userId y items son obligatorios")
            );
        }

        // Crear venta y descontar stock
        Sale sale = saleService.createSale(request);

        // Respuesta limpia
        return ResponseEntity.ok(
                Map.of(
                        "saleId", sale.getId(),
                        "status", "PENDING",
                        "message", "Venta creada correctamente"
                )
        );
    }
}
