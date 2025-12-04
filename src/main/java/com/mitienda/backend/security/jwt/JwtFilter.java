package com.mitienda.backend.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // ===============================================================
    // 1) RUTAS QUE NO DEBEN SER FILTRADAS POR JWT (rutas públicas)
    // ===============================================================
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();

        return path.matches("^/api/v1/auth(/.*)?$")
                || path.matches("^/api/v1/products(/.*)?$")
                || path.matches("^/api/v1/transbank(/.*)?$")
                || path.matches("^/swagger-ui(/.*)?$")
                || path.matches("^/v3/api-docs(/.*)?$");
    }

    // ===============================================================
    // 2) FILTRADO JWT PARA RUTAS PROTEGIDAS
    // ===============================================================
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 🔥 Si NO hay token → dejar pasar (solo rutas protegidas llegarán aquí)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔥 Hay token → procesarlo
        String token = authHeader.substring(7);

        if (jwtUtil.validateToken(token)) {

            String email = jwtUtil.getEmailFromToken(token);
            String rol = jwtUtil.getRolFromToken(token);

            SimpleGrantedAuthority authority =
                    new SimpleGrantedAuthority("ROLE_" + rol);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            Collections.singletonList(authority)
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
