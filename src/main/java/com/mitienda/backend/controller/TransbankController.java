package com.mitienda.backend.controller;

import com.mitienda.backend.dto.TransbankInitRequest;
import com.mitienda.backend.dto.TransbankInitResponse;
import com.mitienda.backend.entity.Sale;
import com.mitienda.backend.repository.SaleRepository;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transbank")
@CrossOrigin("*")
public class TransbankController {

    private final SaleRepository saleRepo;

    public TransbankController(SaleRepository saleRepo) {
        this.saleRepo = saleRepo;
    }

    @PostMapping("/init")
    public TransbankInitResponse iniciarPago(@RequestBody TransbankInitRequest req) {

        Sale sale = saleRepo.findById(req.getSaleId())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        String url = "http://localhost:8080/api/v1/transbank/return?token_ws=" + sale.getId();

        return new TransbankInitResponse(url);
    }

    @GetMapping("/return")
    public void retornoTransbank(
            @RequestParam("token_ws") Long saleId,
            @RequestParam(defaultValue = "APROBADO") String status,
            HttpServletResponse response) throws IOException {

        Sale sale = saleRepo.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        sale.setEstado(status.equalsIgnoreCase("APROBADO") ? "APROBADO" : "RECHAZADO");
        saleRepo.save(sale);

       response.sendRedirect("http://localhost:5173/#/checkout/success?saleId=" + saleId);

    }
}
