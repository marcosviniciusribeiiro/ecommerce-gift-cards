package com.dao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "O nome é obrigatório!")
    @Size(max = 80, message = "O nome deve possui no máximo 80 caracteres")
    private String nome;

    @NotBlank(message = "O email é obrigatório!")
    @Email(message = "O email informado é inválido.")
    @Size(max = 120, message = "O email deve possui no máximo 120 caracteres")
    private String email;

    @NotBlank(message = "A senha é obrigatória!")
    @Size(min = 8, max = 50, message = "A senha deve possuir entre 8 a 50 caracteres")
    private String senha;

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
