package com.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CodigoGiftCardRequest {
    @NotBlank(message = "O ID do produto é obrigatório.")
    @Positive(message = "O ID do produto deve ser maior que zero.")
    private Integer idProduto;

    @NotBlank(message = "O código é obrigatório.")
    private String codigo;

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
