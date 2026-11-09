package com.service;

import com.dto.asaas.CobrancaAsaasResponse;
import com.model.*;
import com.repository.EventoWebhookRepository;
import com.repository.PagamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WebhookProcessamentoService {
    private final EventoWebhookRepository eventoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final AsaasService asaasService;
    private final ConfirmacaoAutomaticaService confirmacaoAutomaticaService;

    public WebhookProcessamentoService(
            EventoWebhookRepository eventoRepository,
            PagamentoRepository pagamentoRepository,
            AsaasService asaasService,
            ConfirmacaoAutomaticaService confirmacaoAutomaticaService
    ) {
        this.eventoRepository = eventoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.asaasService = asaasService;
        this.confirmacaoAutomaticaService = confirmacaoAutomaticaService;
    }

    @Transactional
    public void atualizarPagamento(String idEventoAsaas) {
        EventoWebhook evento = eventoRepository.findById(idEventoAsaas)
                .orElseThrow(
                        () -> new IllegalArgumentException("Evento de webhook não encontrado.")
                );

        if (evento.getStatus() == StatusEventoWebhook.PROCESSADO) {
            return;
        }

        // Aceitar somente eventos de confirmação
        if (!evento.getTipoEvento().equals("PAYMENT_RECEIVED") &&
                !evento.getTipoEvento().equals("PAYMENT_CONFIRMED")) {
            throw new IllegalStateException("Este evento não representa uma confirmação de pagamento.");
        }

        Pagamento pagamento = pagamentoRepository.findByIdPagamentoAsaas(evento.getIdPagamentoAsaas())
                .orElseThrow(
                        () -> new IllegalArgumentException("Pagamento não encontrado no ecommerce.")
                );

        CobrancaAsaasResponse cobranca = asaasService.consultarCobranca(pagamento.getIdPagamentoAsaas());

        if (!pagamento.getIdPagamentoAsaas().equals(cobranca.id())) {
            throw new IllegalStateException("Identificador da cobrança divergente.");
        }

        if (cobranca.value() == null ||
                pagamento.getValor().compareTo(cobranca.value()) != 0) {
            throw new IllegalStateException("O valor da cobrança não corresponde ao pagamento.");
        }

        String referenciaEsperada = "PEDIDO-" + pagamento.getPedido().getId();

        if (!referenciaEsperada.equals(cobranca.externalReference())) {
            throw new IllegalStateException("A cobrança não corresponde ao pedido.");
        }

        if (pagamento.getFormaPagamento() != FormaPagamento.PIX ||
                !"PIX".equals(cobranca.billingType())) {
            throw new IllegalStateException("Forma de pagamento incompatível.");
        }

        StatusPagamento novoStatus;

        if ("RECEIVED".equals(cobranca.status())) {
            novoStatus = StatusPagamento.RECEIVED;
        } else if ("CONFIRMED".equals(cobranca.status())) {
            novoStatus = StatusPagamento.CONFIRMED;
        } else {
            throw new IllegalStateException("Pagamento ainda não confirmado no Asaas.");
        }

        if (pagamento.getStatus() != StatusPagamento.RECEIVED) {
            pagamento.setStatus(novoStatus);
        }

        if (pagamento.getDataConfirmacao() == null) {
            pagamento.setDataConfirmacao(LocalDateTime.now());
        }

        pagamentoRepository.save(pagamento);
    }

    public void processarConfirmacao(String idEvento) {
        EventoWebhook evento = eventoRepository.findById(idEvento)
                .orElseThrow(
                        () -> new IllegalArgumentException("Evento não encontrado.")
                );

        if (evento.getStatus() == StatusEventoWebhook.PROCESSADO) return;

        if (!"PAYMENT_RECEIVED".equals(evento.getTipoEvento())
                && !"PAYMENT_CONFIRMED".equals(evento.getTipoEvento())) {
            throw new IllegalStateException("Tipo de evento não suportado.");
        }

        Pagamento pagamento = pagamentoRepository.findByIdPagamentoAsaas(evento.getIdPagamentoAsaas())
                .orElseThrow(
                        () -> new IllegalArgumentException("Pagamento não encontrado.")
                );

        CobrancaAsaasResponse cobranca = asaasService.consultarCobranca(pagamento.getIdPagamentoAsaas());

        confirmacaoAutomaticaService.concluirConfirmacao(idEvento, cobranca);
    }
}
