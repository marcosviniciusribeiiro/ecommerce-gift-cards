package com.controller;

import com.dto.LoginRequest;
import com.dto.LoginResponse;
import com.dto.UsuarioRequest;
import com.dto.UsuarioResponse;
import com.model.Usuario;
import com.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {
    public final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/")
    public Usuario retornar(@RequestParam String email){
        return service.buscarPorEmail(email)
                .orElse(null);
    }

    @PostMapping("/new")
    public UsuarioResponse cadastrar(@Valid @RequestBody UsuarioRequest request) {

        Usuario usuario = service.cadastrar(request);

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario(),
                usuario.getDataCadastro()
        );
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request){
        return service.login(
                request.getEmail(),
                request.getSenha()
        );
    }
}
