package com.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RedefinirSenhaRequest {
    @NotBlank(message = "O token é obrigatório.")
    private String token;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 8, max = 32, message = "A senha deve possuir entre 8 a 32 caracteres.")
    private String senha;

    private String confirmarSenha;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }
}
