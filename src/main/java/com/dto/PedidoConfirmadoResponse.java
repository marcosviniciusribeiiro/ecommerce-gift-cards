package com.dto;

import com.model.StatusPedido;

import java.math.BigDecimal;
import java.util.List;

public class PedidoConfirmadoResponse {
    private Integer idPedido;
    private StatusPedido status;
    private BigDecimal valorTotal;
    private List<Integer> idsCodigos;

    public PedidoConfirmadoResponse(
            Integer idPedido,
            StatusPedido status,
            BigDecimal valorTotal,
            List<Integer> idsCodigos
    ) {
        this.idPedido = idPedido;
        this.status = status;
        this.valorTotal = valorTotal;
        this.idsCodigos = idsCodigos;
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

    public List<Integer> getIdsCodigos() {
        return idsCodigos;
    }
}
