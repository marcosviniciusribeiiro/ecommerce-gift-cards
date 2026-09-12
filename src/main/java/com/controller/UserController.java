package com.controller;

import com.dto.LoginRequest;
import com.dto.LoginResponse;
import com.dto.UserRequest;
import com.dto.UserResponse;
import com.model.Usuario;
import com.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    public final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/")
    public Usuario retornar(@RequestParam String email){
        return service.buscarPorEmail(email)
                .orElse(null);
    }

    @PostMapping("/new")
    public UserResponse cadastrar(@Valid @RequestBody UserRequest request) {

        Usuario usuario = service.cadastrar(request);

        return new UserResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUser(),
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
