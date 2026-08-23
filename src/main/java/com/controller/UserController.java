package com.controller;

import com.dao.UserRequest;
import com.dao.UserResponse;
import com.model.User;
import com.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public UserResponse cadastrar(@RequestBody UserRequest request) {

        User user = userService.cadastrar(request);

        return new UserResponse(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getTipoUser(),
                user.getDataCadastro()
        );
    }

    @GetMapping("/users")
    public User findByEmail(@RequestParam String email){
        return userService.buscarPorEmail(email).orElse(null);
    }
}
