package com.example.demo.dto;

public class RegistroRequest {
    private String nome;
    private String email;
    private String senha;

    public RegistroRequest() {
    }

    public RegistroRequest(String email, String senha, String nome) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}