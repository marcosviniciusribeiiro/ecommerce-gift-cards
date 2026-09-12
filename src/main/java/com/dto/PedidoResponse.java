package com.dto;

import com.model.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoResponse {
    private Integer id;

    private Integer idProduto;

    private StatusPedido statusPedido;

    private LocalDateTime dataPedido;

    private BigDecimal valorTotal;

    public PedidoResponse(
            Integer id,
            Integer idProduto,
            StatusPedido statusPedido,
            LocalDateTime dataPedido,
            BigDecimal valorTotal
    ) {
        this.id = id;
        this.idProduto = idProduto;
        this.statusPedido = statusPedido;
        this.dataPedido = dataPedido;
        this.valorTotal = valorTotal;
    }


}
