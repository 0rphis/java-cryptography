package com.example.demo.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey chaveSecreta;
    private final long expiracaoMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expiracaoMs) {

        byte[] chaveEmBytes = Decoders.BASE64.decode(secret);
        this.chaveSecreta = Keys.hmacShaKeyFor(chaveEmBytes);
        this.expiracaoMs = expiracaoMs;
    }

    public String gerarToken(String email) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + expiracaoMs);

        return Jwts.builder()
                .subject(email)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(chaveSecreta)
                .compact();
    }

    public String extrairEmail(String token) {
        return Jwts.parser()
                .verifyWith(chaveSecreta)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}