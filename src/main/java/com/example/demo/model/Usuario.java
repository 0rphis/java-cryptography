package com.example.demo.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity

public class Usuario {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
    }

    public void setSenha(String senhaOriginal) {
        if (senhaOriginal == null || senhaOriginal.isEmpty()) {
            throw new IllegalArgumentException("A senha nao pode ser nula ou vazia");
        } else {
            this.senha = encoder.encode(senhaOriginal);
        }
    }

    public boolean verificarSenha(String senhaOriginal) {
        return senhaOriginal != null
                && this.senha != null
                && encoder.matches(senhaOriginal, this.senha);
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}