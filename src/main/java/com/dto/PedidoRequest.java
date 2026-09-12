package com.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PedidoRequest {
    @NotNull(message = "O produto é obrigatório.")
    @Positive(message = "O ID do produto deve ser maior do que zero.")
    private Integer idProduto;

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }
}
