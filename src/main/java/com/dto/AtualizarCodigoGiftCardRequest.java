package com.dto;

import jakarta.validation.constraints.NotBlank;

public class AtualizarCodigoGiftCardRequest {

    @NotBlank(message = "O código não pode estar vazio.")
    private String codigo;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}