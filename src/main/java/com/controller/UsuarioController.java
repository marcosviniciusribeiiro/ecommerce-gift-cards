package com.controller;

import com.dto.LoginRequest;
import com.dto.LoginResponse;
import com.dto.UsuarioRequest;
import com.dto.UsuarioResponse;
import com.model.Usuario;
import com.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
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

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Integer id){
        return null;
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
