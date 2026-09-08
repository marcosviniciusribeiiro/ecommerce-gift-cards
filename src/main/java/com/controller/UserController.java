package com.controller;

import com.dao.LoginRequest;
import com.dao.LoginResponse;
import com.dao.UserRequest;
import com.dao.UserResponse;
import com.model.User;
import com.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public User retornar(@RequestParam String email){
        return userService.buscarPorEmail(email).orElse(null);
    }

    @PostMapping("/users/new")
    public UserResponse cadastrar(@Valid @RequestBody UserRequest request) {

        User user = userService.cadastrar(request);

        return new UserResponse(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getTipoUser(),
                user.getDataCadastro()
        );
    }

    @PostMapping("/user/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request){
        return userService.login(request.getEmail(), request.getSenha());
    }
}
