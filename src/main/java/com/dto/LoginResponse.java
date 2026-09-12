package com.dto;

import com.model.TipoUsuario;

public class LoginResponse {
    private Integer id;
    private String nome;
    private String email;
    private TipoUsuario tipoUsuario;
    private String token;

    public LoginResponse(
            Integer id,
            String nome,
            String email,
            TipoUsuario tipoUsuario,
            String token
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
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

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public String getToken() { return token; }
}
