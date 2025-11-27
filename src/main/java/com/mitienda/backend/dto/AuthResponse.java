package com.mitienda.backend.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String email;
    private String rol;
    private Long userId;

    public AuthResponse(String token, String email, String rol, Long userId) {
        this.token = token;
        this.email = email;
        this.rol = rol;
        this.userId = userId;
    }
}
