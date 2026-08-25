package com.dao;

import com.model.Tipo_User;

public class LoginResponse {
    private Integer id;
    private String nome;
    private String email;
    private Tipo_User tipo_User;

    public LoginResponse(Integer id, String nome, String email, Tipo_User tipo_User) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo_User = tipo_User;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Tipo_User getTipo_User() {
        return tipo_User;
    }

    public void setTipo_User(Tipo_User tipo_User) {
        this.tipo_User = tipo_User;
    }
}
