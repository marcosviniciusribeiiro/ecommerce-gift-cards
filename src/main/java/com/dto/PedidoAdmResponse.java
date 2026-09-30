package com.dto;

import com.model.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoAdmResponse {
    private Integer idPedido;
    private Integer idUsuario;
    private String nomeUsuario;
    private String emailUsuario;
    private Integer idProduto;
    private String nomeProduto;
    private StatusPedido status;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal;

    public PedidoAdmResponse(
            Integer idPedido,
            Integer idUsuario,
            String nomeUsuario,
            String emailUsuario,
            Integer idProduto,
            String nomeProduto,
            StatusPedido status,
            LocalDateTime dataPedido,
            BigDecimal valorTotal
    ) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
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

    public Integer getIdProduto() {
        return idProduto;
    }

    public String getNomeProduto() { return nomeProduto; }

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
