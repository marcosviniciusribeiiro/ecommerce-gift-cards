package com.dto;

import java.util.List;

public class CodigoCompradoResponse {
    private Integer idPedido;
    private List<ItemCodigoCompradoResponse> itens;

    public CodigoCompradoResponse(Integer idPedido, List<ItemCodigoCompradoResponse> itens) {
        this.idPedido = idPedido;
        this.itens = itens;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public List<ItemCodigoCompradoResponse> getItens() {
        return itens;
    }

    public void setItens(List<ItemCodigoCompradoResponse> itens) {
        this.itens = itens;
    }
}
