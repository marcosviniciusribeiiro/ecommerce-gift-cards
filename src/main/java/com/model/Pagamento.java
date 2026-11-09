package com.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagamento")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @Column(name = "id_pagamento_asaas", unique = true, length = 100)
    private String idPagamentoAsaas;

    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false, length = 30)
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusPagamento status;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_confirmacao")
    private LocalDateTime dataConfirmacao;

    protected Pagamento() {
    }

    public Pagamento (
            Pedido pedido,
            BigDecimal valor,
            FormaPagamento formaPagamento
    ) {
        this.pedido = pedido;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.status = StatusPagamento.PENDING;
        this.dataCriacao = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getIdPagamentoAsaas() {
        return idPagamentoAsaas;
    }

    public void setIdPagamentoAsaas(String idPagamentoAsaas) {
        this.idPagamentoAsaas = idPagamentoAsaas;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataConfirmacao() {
        return dataConfirmacao;
    }

    public void setDataConfirmacao(LocalDateTime dataConfirmacao) {
        this.dataConfirmacao = dataConfirmacao;
    }
}
