package com.controller;

import com.dto.*;
import com.model.TokenRecuperacao;
import com.model.Usuario;
import com.service.TokenRecuperacaoService;
import com.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    public final UsuarioService usuarioService;
    public final TokenRecuperacaoService tokenRecuperacaoService;

    public UsuarioController(UsuarioService usuarioService,
                             TokenRecuperacaoService tokenRecuperacaoService
    ) {
        this.usuarioService = usuarioService;
        this.tokenRecuperacaoService = tokenRecuperacaoService;
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> retornarUsuario(Authentication authentication) {
        Usuario usuario = usuarioService.buscarUsuarioAutenticado(
                authentication.getName()
        );

        return ResponseEntity
                .ok(
                        converterParaResponse(usuario)
                );
    }

    @PostMapping("/new")
    public ResponseEntity<UsuarioResponse> cadastrar(
            @Valid @RequestBody UsuarioRequest request
    ) {
        Usuario usuario = usuarioService.cadastrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(converterParaResponse(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse login = usuarioService.login(
                request.getEmail(),
                request.getSenha()
        );

        return ResponseEntity.ok(login);
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponse> atualizar(
            @Valid @RequestBody UsuarioRequest request,
            Authentication authentication) {
        String email = authentication.getName();

        Usuario usuario = usuarioService.atualizar(
                email,
                request
        );

        return ResponseEntity.ok(
                converterParaResponse(usuario)
        );
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deletar(Authentication authentication){
        String email = authentication.getName();

        usuarioService.deletar(email);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<RecuperarSenhaResponse> recuperarSenha(
            @Valid @RequestBody RecuperarSenhaRequest request){
        TokenRecuperacao token = tokenRecuperacaoService.gerarToken(request.getEmail());

        RecuperarSenhaResponse response = new RecuperarSenhaResponse(
                token.getToken(),
                token.getDataExpiracao()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(
            @Valid @RequestBody RedefinirSenhaRequest request
    ){
        tokenRecuperacaoService.redefinirSenha(request.getToken(), request.getNovaSenha());

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