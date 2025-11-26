package com.mitienda.backend.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String email;
    private String rol;

    public AuthResponse(String token, String email, String rol) {
        this.token = token;
        this.email = email;
        this.rol = rol;
    }
}
