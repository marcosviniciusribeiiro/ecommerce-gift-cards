package com.controller;

import com.dto.LoginRequest;
import com.dto.LoginResponse;
import com.dto.UsuarioRequest;
import com.dto.UsuarioResponse;
import com.exception.UserNotFoundException;
import com.model.Usuario;
import com.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    public final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> retornarUsuario(Authentication authentication){
        Usuario usuario = service.buscarUsuarioAutenticado(
                authentication.getName()
        );

        return ResponseEntity
                .ok()
                .body(converterParaResponse(usuario));
    }

    @PostMapping("/new")
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody UsuarioRequest request) {

        Usuario usuario = service.cadastrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(converterParaResponse(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        LoginResponse login = service.login(
                request.getEmail(),
                request.getSenha()
        );

        return ResponseEntity.ok(login);
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponse> atualizar(@Valid @RequestBody UsuarioRequest request, Authentication authentication){

        String email = authentication.getName();

        Usuario usuario = service.atualizar(
                email,
                request
        );

        return ResponseEntity
                .ok(converterParaResponse(usuario));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deletar(Authentication authentication){
        String email = authentication.getName();

        service.deletar(email);

        return ResponseEntity
                .noContent()
                .build();
    }

    public UsuarioResponse converterParaResponse(Usuario usuario){
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario(),
                usuario.getDataCadastro()
        );
    }
}
