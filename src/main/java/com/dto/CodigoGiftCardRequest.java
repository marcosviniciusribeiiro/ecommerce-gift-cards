package com.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CodigoGiftCardRequest {
    @NotNull(message = "O ID do produto é obrigatório.")
    @Positive(message = "O ID do produto deve ser maior que zero.")
    private Integer id;

    @NotBlank(message = "O código é obrigatório.")
    private String codigoGiftCard;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoGiftCard() {
        return codigoGiftCard;
    }

    public void setCodigoGiftCard(String codigoGiftCard) {
        this.codigoGiftCard = codigoGiftCard;
    }
}
