package com.dto;

import com.model.Plataforma;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProdutoRequest {
    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(max = 80, message = "O nome deve possuir no máximo 80 caracteres.")
    private String nome;

    @Size(max = 255, message = "A descrição deve possuir no máximo 255 caracteres.")
    private String descricao;

    @NotBlank(message = "A plataforma é obrigatória.")
    private Plataforma plataforma;

    @NotNull(message = "O valor é obrigatório.")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Plataforma getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(Plataforma plataforma) {
        this.plataforma = plataforma;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}