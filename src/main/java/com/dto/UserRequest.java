package com.dto;

import jakarta.validation.constraints.*;

public class UserRequest {

    @NotNull(message = "O nome é obrigatório.")
    @Size(max = 80, message = "O nome deve possui no máximo 80 caracteres.")
    private String nome;

    @NotNull(message = "O email é obrigatório.")

    @Email(message = "O email informado é inválido.")
    @Size(max = 120, message = "O email deve possui no máximo 120 caracteres.")
    //Exige uma extensão válida para o email
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z0]{2,}$",
            message = "O e-mail deve possuir um domínio válido, como teste@email.com.")
    private String email;

    @NotNull(message = "A senha é obrigatória.")
    @Size(min = 8, max = 32, message = "A senha deve possuir entre 8 a 32 caracteres.")
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
