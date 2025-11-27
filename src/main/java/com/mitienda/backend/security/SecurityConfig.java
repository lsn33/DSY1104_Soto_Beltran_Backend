package com.mitienda.backend.security;

import com.mitienda.backend.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf(csrf -> csrf.disable())
        .cors(cors -> {})
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
                // 🔓 PÚBLICOS
                .requestMatchers("/api/v1/auth/login").permitAll()
                .requestMatchers("/api/v1/auth/register").permitAll()
                .requestMatchers("/api/v1/transbank/**").permitAll()

                // 🔓 PRODUCTOS — PÚBLICOS
                .requestMatchers("/api/v1/products").permitAll()
                .requestMatchers("/api/v1/products/**").permitAll()

                // 🔓 CATEGORÍAS — PÚBLICAS
                .requestMatchers("/api/v1/categories").permitAll()
                .requestMatchers("/api/v1/categories/**").permitAll()

                // 🔒 ÓRDENES — SOLO ADMIN
                .requestMatchers("/api/v1/sales").hasRole("ADMIN")
                .requestMatchers("/api/v1/sales/**").hasRole("ADMIN")

                // 🔒 TODO LO DEMÁS — REQUIERE TOKEN
                .anyRequest().authenticated()
        );

    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}


}

