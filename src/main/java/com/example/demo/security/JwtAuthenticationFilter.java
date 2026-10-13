package com.example.demo.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);

        try {
            String email = jwtService.extrairEmail(token);

            UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    Collections.emptyList());

            SecurityContextHolder.getContext()
                    .setAuthentication(autenticacao);

        } catch (JwtException exception) {
            System.out.println(
                    "Erro JWT: "
                            + exception.getClass().getSimpleName()
                            + " - "
                            + exception.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido ou expirado.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}