package com.mitienda.backend.controller;

import com.mitienda.backend.dto.SaleRequest;
import com.mitienda.backend.entity.Sale;
import com.mitienda.backend.repository.SaleRepository;
import com.mitienda.backend.service.SaleService;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales")
@CrossOrigin("*")
public class SaleController {

    private final SaleService saleService;
    private final SaleRepository saleRepository;

    public SaleController(SaleService saleService, SaleRepository saleRepository) {
        this.saleService = saleService;
        this.saleRepository = saleRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SaleRequest request) {
        Sale sale = saleService.createSale(request);
        return ResponseEntity.ok(Map.of("saleId", sale.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return saleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
