package com.dto;

import com.model.Tipo_User;

import java.time.LocalDate;

public class UserResponse {
    private Integer id;
    private String nome;
    private String email;
    private Tipo_User tipoUser;
    private LocalDate dataCadastro;

    public UserResponse(
            Integer id,
            String nome,
            String email,
            Tipo_User tipoUser,
            LocalDate dataCadastro
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipoUser = tipoUser;
        this.dataCadastro = dataCadastro;
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

    public Tipo_User getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(Tipo_User tipoUser) {
        this.tipoUser = tipoUser;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
