package com.mitienda.backend.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // ===============================================================
    // GENERAR TOKEN – SIEMPRE FORMATEAR EL ROL COMO ROLE_ADMIN/ROLE_USER
    // ===============================================================
    public String generateToken(String email, String rol) {

        // Agregar prefijo ROLE_ solo si no viene incluido
        String formattedRole = rol.startsWith("ROLE_") ? rol : "ROLE_" + rol;

        return Jwts.builder()
                .setSubject(email)
                .claim("rol", formattedRole)   // ahora guarda ROLE_ADMIN o ROLE_USER
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día
                .signWith(secretKey)
                .compact();
    }

    // ===============================================================
    // OBTENER EMAIL
    // ===============================================================
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ===============================================================
    // OBTENER ROL (YA FORMATEADO)
    // ===============================================================
    public String getRolFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("rol", String.class);   // ya viene ROLE_ADMIN o ROLE_USER
    }

    // ===============================================================
    // VALIDAR TOKEN
    // ===============================================================
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
