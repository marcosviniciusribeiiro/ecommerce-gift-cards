package com.dto;

import com.model.Plataforma;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(max = 80, message = "O nome deve possui no máximo 80 caracteres.")
    private String nome;

    @NotBlank(message = "A descrição do produto é obrigatória.")
    @Size(max = 80, message = "A descrição do produto deve possui no máximo 255 caracteres.")
    private String descricao;

    @NotBlank(message = "A plataforma do produto é obrigátoria.")
    private Plataforma plataforma;

    @NotBlank(message = "O valor do produto é obrigatório.")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior do que zero.")
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
