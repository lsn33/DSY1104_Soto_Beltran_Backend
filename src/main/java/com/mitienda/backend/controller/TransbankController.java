package com.mitienda.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mitienda.backend.transbank.WebpayService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/transbank")
@CrossOrigin("*")
public class TransbankController {

    private final WebpayService webpayService;

    public TransbankController(WebpayService webpayService) {
        this.webpayService = webpayService;
    }

    @PostMapping("/init")
    public JsonNode init(@RequestBody Map<String, Object> req) throws Exception {

        // 🔥 LOG PARA SABER QUÉ RECIBE BACKEND
        System.out.println("\n===============================");
        System.out.println("TRANSBANK /init — BODY RECIBIDO:");
        System.out.println(req);
        System.out.println("===============================\n");

        // 🔥 Validaciones para evitar NullPointerException
        if (req == null || req.isEmpty()) {
            throw new IllegalArgumentException("El body llegó vacío a /init");
        }

        if (!req.containsKey("buyOrder") || !req.containsKey("sessionId") || !req.containsKey("amount")) {
            throw new IllegalArgumentException("Faltan campos en el body. Se requiere: buyOrder, sessionId, amount");
        }

        String buyOrder = req.get("buyOrder").toString();
        String sessionId = req.get("sessionId").toString();

        double amount;
        try {
            amount = Double.parseDouble(req.get("amount").toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("El campo 'amount' no es numérico.");
        }

        String returnUrl = "http://localhost:5173/webpay/return";

        return webpayService.initTransaction(buyOrder, sessionId, amount, returnUrl);
    }

    @GetMapping("/return")
    public JsonNode finish(@RequestParam("token_ws") String token) throws Exception {
        return webpayService.commitTransaction(token);
    }

    @GetMapping("/status/{token}")
    public JsonNode status(@PathVariable String token) throws Exception {
        return webpayService.getStatus(token);
    }
}
