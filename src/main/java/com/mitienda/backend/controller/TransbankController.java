package com.mitienda.backend.controller;

import com.mitienda.backend.dto.TransbankInitRequest;
import com.mitienda.backend.dto.TransbankInitResponse;
import com.mitienda.backend.entity.Sale;
import com.mitienda.backend.repository.SaleRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transbank")
@CrossOrigin("*")
public class TransbankController {

    private final SaleRepository saleRepo;

    public TransbankController(SaleRepository saleRepo) {
        this.saleRepo = saleRepo;
    }

    // 1) Simulación inicio de pago
    @PostMapping("/init")
    public TransbankInitResponse iniciarPago(@RequestBody TransbankInitRequest req) {

        Sale sale = saleRepo.findById(req.getSaleId())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        // Creamos un URL ficticio hacia backend
        String url = "http://localhost:8080/api/v1/transbank/return?token_ws=" +
                sale.getId();

        return new TransbankInitResponse(url);
    }

    // 2) Simulación retorno de Transbank
    @GetMapping("/return")
    public String retornoTransbank(@RequestParam("token_ws") Long saleId,
                                   @RequestParam(defaultValue = "APROBADO") String status) {

        Sale sale = saleRepo.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        if (status.equalsIgnoreCase("APROBADO")) {
            sale.setEstado("APROBADO");
        } else {
            sale.setEstado("RECHAZADO");
        }

        saleRepo.save(sale);

        return "Estado actualizado a: " + sale.getEstado();
    }
}
