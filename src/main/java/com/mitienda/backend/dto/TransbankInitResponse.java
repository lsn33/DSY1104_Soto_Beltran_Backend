package com.mitienda.backend.dto;

import lombok.Data;

@Data
public class TransbankInitResponse {
    private String urlRedireccion;

    public TransbankInitResponse(String url) {
        this.urlRedireccion = url;
    }
}
