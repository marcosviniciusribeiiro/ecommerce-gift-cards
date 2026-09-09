package com.controller;

import com.dto.LoginRequest;
import com.dto.LoginResponse;
import com.dto.UserRequest;
import com.dto.UserResponse;
import com.model.User;
import com.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public User retornar(@RequestParam String email){
        return userService.buscarPorEmail(email).orElse(null);
    }

    @PostMapping("/new")
    public UserResponse cadastrar(@Valid @RequestBody UserRequest request) {

        User usuario = userService.cadastrar(request);

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
        return userService.login(
                request.getEmail(),
                request.getSenha()
        );
    }
}
