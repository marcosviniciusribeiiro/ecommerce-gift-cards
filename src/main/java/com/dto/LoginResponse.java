package com.dto;

import com.model.TipoUser;

public class LoginResponse {
    private Integer id;
    private String nome;
    private String email;
    private TipoUser tipoUser;
    private String token;

    public LoginResponse(
            Integer id,
            String nome,
            String email,
            TipoUser tipoUser,
            String token
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipoUser = tipoUser;
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

    public TipoUser getTipoUser() {
        return tipoUser;
    }

    public String getToken() { return token; }
}
