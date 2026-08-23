package com.controller;

import com.dao.UserRequest;
import com.model.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User cadastrar(@RequestBody UserRequest request){
        return userService.cadastrar(request);
    }

    @GetMapping("/users")
    public User findByEmail(@RequestParam String email){
        return userService.buscarPorEmail(email).orElse(null);
    }
}
