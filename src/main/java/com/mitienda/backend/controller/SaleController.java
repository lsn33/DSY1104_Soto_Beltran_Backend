package com.mitienda.backend.controller;

import com.mitienda.backend.dto.SaleRequest;
import com.mitienda.backend.entity.Sale;
import com.mitienda.backend.service.SaleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales")
@CrossOrigin("*")
public class SaleController {

    private final SaleService service;

    public SaleController(SaleService service) {
        this.service = service;
    }

    @PostMapping
    public Sale createSale(@RequestBody SaleRequest request) {
        return service.createSale(request);
    }
}
