package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegistroRequest;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

@Service

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean autenticar(LoginRequest loginRequest) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail());
        if (usuario != null) {
            return usuario.verificarSenha(loginRequest.getSenha());
        }
        return false;
    }

    public Usuario cadastrar(RegistroRequest registroRequest) {
        Usuario usuarioExistente = usuarioRepository
                .findByEmail(registroRequest.getEmail());

        if (usuarioExistente != null) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(registroRequest.getNome());
        usuario.setEmail(registroRequest.getEmail());
        usuario.setSenha(registroRequest.getSenha()); // gera o hash bcrypt

        return usuarioRepository.save(usuario);
    }
}
