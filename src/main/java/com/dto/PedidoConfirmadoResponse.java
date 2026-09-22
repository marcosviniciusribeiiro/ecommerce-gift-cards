package com.dto;

import com.model.StatusPedido;

import java.math.BigDecimal;

public class PedidoConfirmadoResponse {
    private Integer idPedido;
    private StatusPedido status;
    private BigDecimal valorTotal;
    private String codigoGiftCard;

    public PedidoConfirmadoResponse(
            Integer idPedido,
            StatusPedido status,
            BigDecimal valorTotal,
            String codigoGiftCard
    ) {
        this.idPedido = idPedido;
        this.status = status;
        this.valorTotal = valorTotal;
        this.codigoGiftCard = codigoGiftCard;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public String getCodigoGiftCard() {
        return codigoGiftCard;
    }
}
