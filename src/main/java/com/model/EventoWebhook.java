package com.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_eventos_webhook")
public class EventoWebhook {
    @Id
    @Column(name = "id_evento_asaas", length = 100)
    private String idEventoAsaas;

    @Column(name = "tipo_evento", length = 80, nullable = false)
    private String tipoEvento;

    @Column(name = "id_pagamento_asaas", length = 100, nullable = false)
    private String idPagamentoAsaas;

    @Column(name = "payload", columnDefinition = "LONGTEXT", nullable = false)
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private StatusEventoWebhook status;

    @Column(name = "data_recebimento", nullable = false)
    private LocalDateTime dataRecebimento;

    @Column(name = "data_processamento")
    private LocalDateTime dataProcessamento;

    protected EventoWebhook() {

    }

    public EventoWebhook(
            String idEventoAsaas,
            String tipoEvento,
            String idPagamentoAsaas,
            String payload
    ) {
        this.idEventoAsaas = idEventoAsaas;
        this.tipoEvento = tipoEvento;
        this.idPagamentoAsaas = idPagamentoAsaas;
        this.payload = payload;
        this.status = StatusEventoWebhook.RECEBIDO;
        this.dataRecebimento = LocalDateTime.now();
    }

    public String getIdEventoAsaas() {
        return idEventoAsaas;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public String getIdPagamentoAsaas() {
        return idPagamentoAsaas;
    }

    public String getPayload() {
        return payload;
    }

    public StatusEventoWebhook getStatus() {
        return status;
    }

    public LocalDateTime getDataRecebimento() {
        return dataRecebimento;
    }

    public LocalDateTime getDataProcessamento() {
        return dataProcessamento;
    }

    public void setStatus(StatusEventoWebhook status) {
        this.status = status;
    }

    public void setDataProcessamento(LocalDateTime dataProcessamento) {
        this.dataProcessamento = dataProcessamento;
    }

    public void marcarComoProcessado() {
        this.status = StatusEventoWebhook.PROCESSADO;
        this.dataProcessamento = LocalDateTime.now();
    }

    public void marcarComoFalha() {
        this.status = StatusEventoWebhook.FALHA;
    }
}
