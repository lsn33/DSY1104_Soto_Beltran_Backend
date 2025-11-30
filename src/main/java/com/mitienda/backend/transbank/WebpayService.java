package com.mitienda.backend.transbank;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebpayService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonNode initTransaction(String buyOrder, String sessionId, double amount, String returnUrl) throws Exception {

        String url = WebpayConfig.API_BASE_URL;

        Map<String, Object> body = new HashMap<>();
        body.put("buy_order", buyOrder);
        body.put("session_id", sessionId);
        body.put("amount", amount);
        body.put("return_url", returnUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Tbk-Api-Key-Id", WebpayConfig.API_KEY_ID);
        headers.set("Tbk-Api-Key-Secret", WebpayConfig.API_KEY_SECRET);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return mapper.readTree(response.getBody());
    }

    public JsonNode commitTransaction(String token) throws Exception {

        String url = WebpayConfig.API_BASE_URL + "/" + token;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Tbk-Api-Key-Id", WebpayConfig.API_KEY_ID);
        headers.set("Tbk-Api-Key-Secret", WebpayConfig.API_KEY_SECRET);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

        return mapper.readTree(response.getBody());
    }

    public JsonNode getStatus(String token) throws Exception {

        String url = WebpayConfig.API_BASE_URL + "/" + token;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Tbk-Api-Key-Id", WebpayConfig.API_KEY_ID);
        headers.set("Tbk-Api-Key-Secret", WebpayConfig.API_KEY_SECRET);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return mapper.readTree(response.getBody());
    }
}
