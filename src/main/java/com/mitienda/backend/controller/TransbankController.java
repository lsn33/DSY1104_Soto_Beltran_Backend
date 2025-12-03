package com.mitienda.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mitienda.backend.entity.Sale;
import com.mitienda.backend.repository.SaleRepository;
import com.mitienda.backend.transbank.WebpayService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transbank")
@CrossOrigin("*")
public class TransbankController {

    private final WebpayService webpayService;
    private final SaleRepository saleRepository;

    public TransbankController(WebpayService webpayService, SaleRepository saleRepository) {
        this.webpayService = webpayService;
        this.saleRepository = saleRepository;
    }

    // =========================================================
    // 🔥 1) INICIAR TRANSACCIÓN WEBPAY
    // =========================================================
    @PostMapping("/init")
    public JsonNode init(@RequestBody Map<String, Object> req) throws Exception {

        Long saleId = Long.valueOf(req.get("saleId").toString());

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        // Calcular monto total (Webpay NO permite decimales)
        double amount = sale.getItems().stream()
                .mapToDouble(i -> i.getSubtotal())
                .sum();

        amount = Math.round(amount);

        String buyOrder = saleId.toString();
        String sessionId = sale.getUserId().toString();
        String returnUrl = "http://localhost:5173/webpay/return";

        return webpayService.initTransaction(buyOrder, sessionId, amount, returnUrl);
    }


    // =========================================================
    // 🔥 2) CONFIRMAR TRANSACCIÓN DESDE WEBPAY (token_ws)
    // =========================================================
    @GetMapping("/return")
    public JsonNode finish(@RequestParam("token_ws") String token) throws Exception {

        // Commit en Webpay (PUT con token_ws)
        JsonNode tx = webpayService.commitTransaction(token);

        // ---- Datos recibidos desde Transbank ----
        String status = tx.get("status").asText();                 // AUTHORIZED / FAILED
        String buyOrder = tx.get("buy_order").asText();            // ID de la venta → saleId
        Long saleId = Long.valueOf(buyOrder);
        int amount = tx.get("amount").asInt();                     // monto final
        String authorizationCode = tx.get("authorization_code").asText();
        int responseCode = tx.get("response_code").asInt();
        String paymentType = tx.get("payment_type_code").asText();
        String cardLast4 = tx.get("card_detail").get("card_number").asText();
        String vci = tx.has("vci") ? tx.get("vci").asText() : "";

        // Buscar venta real
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        // ---- Actualizar venta con datos reales ----
        sale.setStatus(status);
        sale.setAmount((double) amount);
        sale.setAuthorizationCode(authorizationCode);
        sale.setResponseCode(responseCode);
        sale.setPaymentTypeCode(paymentType);
        sale.setCardLast4(cardLast4);
        sale.setBuyOrder(buyOrder);
        sale.setToken(token);
        sale.setTransactionDate(LocalDateTime.now());

        saleRepository.save(sale);

        return tx;
    }
}
