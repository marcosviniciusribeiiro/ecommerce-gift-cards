package com.dto;

import com.model.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponse {
    private Integer id;
    private List<ItemPedidoResponse> itens;
    private StatusPedido statusPedido;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal;

    public PedidoResponse(
            Integer id,
            List<ItemPedidoResponse> itens,
            StatusPedido statusPedido,
            LocalDateTime dataPedido,
            BigDecimal valorTotal
    ) {
        this.id = id;
        this.itens = itens;
        this.statusPedido = statusPedido;
        this.dataPedido = dataPedido;
        this.valorTotal = valorTotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ItemPedidoResponse> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoResponse> itens) {
        this.itens = itens;
    }

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
}