package com.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CriarPedidoCarrinhoRequest {

    @Valid
    @NotEmpty(message = "O carrinho não pode estar vazio.")
    private List<ItemCarrinhoRequest> itens;

    public List<ItemCarrinhoRequest> getItens() {
        return itens;
    }

    public void setItens(List<ItemCarrinhoRequest> itens) {
        this.itens = itens;
    }
}
