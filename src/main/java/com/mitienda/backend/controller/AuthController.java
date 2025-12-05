package com.mitienda.backend.controller;

import com.mitienda.backend.dto.*;
import com.mitienda.backend.entity.User;
import com.mitienda.backend.repository.UserRepository;
import com.mitienda.backend.security.jwt.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepo, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {

        User user = new User();
        user.setNombre(req.getNombre());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRol("USER");
        user.setTelefono(req.getTelefono());
        user.setDireccion(req.getDireccion());

        userRepo.save(user);

        return "Usuario registrado con éxito";
    }

    @PostMapping("/login")
public AuthResponse login(@RequestBody LoginRequest req) {

    User user = userRepo.findByEmail(req.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    if (!encoder.matches(req.getPassword(), user.getPassword())) {
        throw new RuntimeException("Contraseña incorrecta");
    }

    String token = jwtUtil.generateToken(user.getEmail(), user.getRol());

    return new AuthResponse(
            token,
            user.getEmail(),
            user.getRol(),
            user.getId()   // ⬅️ ESTO FALTABA
    );
}

}
