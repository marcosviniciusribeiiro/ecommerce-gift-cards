package com.dto;

import com.model.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoAdmResponse {
    private Integer idPedido;

    private Integer idUsuario;
    private String nomeUsuario;
    private String emailUsuario;

    private List<ItemPedidoResponse> itens;

    private StatusPedido status;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal;

    public PedidoAdmResponse(
            Integer idPedido,
            Integer idUsuario,
            String nomeUsuario,
            String emailUsuario,
            List<ItemPedidoResponse> itens,
            StatusPedido status,
            LocalDateTime dataPedido,
            BigDecimal valorTotal
    ) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
        this.itens = itens;
        this.status = status;
        this.dataPedido = dataPedido;
        this.valorTotal = valorTotal;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public List<ItemPedidoResponse> getItens() {
        return itens;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }
}
