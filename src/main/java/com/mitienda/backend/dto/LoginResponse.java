package com.mitienda.backend.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String rol;
    private Long userId;
    private String nombre;
    private String email;

    public LoginResponse(String token, String rol, Long userId, String nombre, String email) {
        this.token = token;
        this.rol = rol;
        this.userId = userId;
        this.nombre = nombre;
        this.email = email;
    }
}
