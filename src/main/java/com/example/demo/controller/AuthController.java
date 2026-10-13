package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegistroRequest;
import com.example.demo.security.JwtService;
import com.example.demo.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class AuthController {
    // Controller methods will be added here in the future
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public AuthController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest) {

        if (usuarioService.autenticar(loginRequest)) {
            String token = jwtService.gerarToken(
                    loginRequest.getEmail());

            return ResponseEntity.ok(new AuthResponse(token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciais inválidas.");
    }

    @PostMapping("/register")
    public ResponseEntity<String> cadastrar(
            @RequestBody RegistroRequest registroRequest) {
        usuarioService.cadastrar(registroRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
    }

    @GetMapping("/teste")
    public ResponseEntity<String> testeToken(Authentication authentication) {
        return ResponseEntity.ok(
                "Token válido. Usuário autenticado: "
                        + authentication.getName());
    }
}
