package com.dao;

import com.model.Tipo_User;

public class LoginResponse {
    private Integer id;
    private String nome;
    private String email;
    private Tipo_User tipo_User;
    private String token;

    public LoginResponse(
            Integer id,
            String nome,
            String email,
            Tipo_User tipo_User,
            String token
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo_User = tipo_User;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public Tipo_User getTipo_User() {
        return tipo_User;
    }

    public String getToken() { return token; }
}
