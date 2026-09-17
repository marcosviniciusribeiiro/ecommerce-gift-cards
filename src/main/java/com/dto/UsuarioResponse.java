package com.dto;

import com.model.TipoUsuario;

import java.time.LocalDate;

public class UsuarioResponse {
    private Integer id;
    private String nome;
    private String email;
    private TipoUsuario tipoUsuario;
    private LocalDate dataCadastro;

    public UsuarioResponse(
            Integer id,
            String nome,
            String email,
            TipoUsuario tipoUsuario,
            LocalDate dataCadastro
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
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

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}