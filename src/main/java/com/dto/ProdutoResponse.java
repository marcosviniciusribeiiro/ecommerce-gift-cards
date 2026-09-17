package com.dto;

import com.model.Plataforma;

import java.math.BigDecimal;

public class ProdutoResponse {
    private Integer id;
    private String nome;
    private String descricao;
    private Plataforma plataforma;
    private BigDecimal valor;

    public ProdutoResponse(
            Integer id,
            String nome,
            String descricao,
            Plataforma plataforma,
            BigDecimal valor
    ) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.plataforma = plataforma;
        this.valor = valor;
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