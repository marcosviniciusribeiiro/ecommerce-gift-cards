package com.dto;

import java.math.BigDecimal;

public class ItemPedidoResponse {
    private Integer idItemPedido;
    private Integer idProduto;
    private String nomeProduto;
    private BigDecimal valorUnitario;

    public ItemPedidoResponse(
            Integer idItemPedido,
            Integer idProduto,
            String nomeProduto,
            BigDecimal valorUnitario
    ) {
        this.idItemPedido = idItemPedido;
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.valorUnitario = valorUnitario;
    }

    public Integer getIdItemPedido() {
        return idItemPedido;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }
}
