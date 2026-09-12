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

    public Integer getId() {
        return id;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
}
